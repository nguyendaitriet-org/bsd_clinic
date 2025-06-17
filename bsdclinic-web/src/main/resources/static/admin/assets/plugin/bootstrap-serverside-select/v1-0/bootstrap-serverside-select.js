/*!
 * Bootstrap Server Select v1.0.0
 * A Bootstrap 5 compatible select component with server-side search
 * Requires: jQuery 3.0+, Bootstrap 5.0+
 * https://github.com/yourname/bootstrap-server-select
 */

(function (factory) {
    if (typeof define === 'function' && define.amd) {
        // AMD
        define(['jquery'], factory);
    } else if (typeof module === 'object' && module.exports) {
        // CommonJS
        module.exports = factory(require('jquery'));
    } else {
        // Browser globals
        factory(jQuery);
    }
}(function ($) {
    'use strict';

    // Plugin defaults
    const DEFAULTS = {
        apiUrl: '/api/search',
        method: 'GET',
        minLength: 2,
        debounceDelay: 300,
        loadingText: 'Searching...',
        noResultsText: 'No results found',
        errorText: 'Failed to fetch results',
        placeholder: 'Search...',
        paramName: 'q',
        displayKey: 'name',
        valueKey: 'id',
        subtitleKey: null,
        allowClear: true,
        autoSelectFirst: false,
        cache: true,
        maxResults: 10,
        headers: {},
        beforeSend: null,
        onSelect: null,
        onClear: null,
        template: null,
        processResults: null
    };

    // Plugin constructor
    function BootstrapServerSelect(element, options) {
        this.element = $(element);
        this.options = $.extend({}, DEFAULTS, options);
        this.cache = {};
        this.selectedIndex = -1;
        this.searchTimeout = null;
        this.currentRequest = null;
        this.isOpen = false;
        
        this.init();
    }

    BootstrapServerSelect.prototype = {
        constructor: BootstrapServerSelect,

        init: function() {
            this.createWrapper();
            this.createDropdown();
            this.createHiddenInput();
            this.bindEvents();
            
            // Load initial data if no minLength or empty string search
            if (this.options.minLength === 0) {
                this.search('');
            }
            
            // Trigger initialized event
            this.element.trigger('bss:initialized', [this]);
        },

        createWrapper: function() {
            if (!this.element.closest('.bs-server-select').length) {
                this.element.wrap('<div class="bs-server-select"></div>');
            }
            
            this.wrapper = this.element.closest('.bs-server-select');
            
            // Add search icon
            if (!this.wrapper.find('.search-icon').length) {
                this.wrapper.append('<i class="bi bi-search search-icon"></i>');
            }
            
            // Set placeholder
            if (this.options.placeholder) {
                this.element.attr('placeholder', this.options.placeholder);
            }
        },

        createDropdown: function() {
            this.dropdown = $('<div class="bs-server-select-dropdown hide"></div>');
            this.wrapper.append(this.dropdown);
        },

        createHiddenInput: function() {
            const name = this.element.attr('name');
            if (name) {
                this.hiddenInput = $('<input type="hidden">').attr('name', name + '_value');
                this.element.removeAttr('name').after(this.hiddenInput);
            }
        },

        bindEvents: function() {
            const self = this;
            
            // Input events
            this.element.on('input.bss', function(e) {
                self.handleInput(e);
            });
            
            this.element.on('focus.bss', function(e) {
                self.handleFocus(e);
            });
            
            this.element.on('keydown.bss', function(e) {
                self.handleKeydown(e);
            });
            
            this.element.on('blur.bss', function(e) {
                // Delay to allow click events on dropdown items
                setTimeout(function() {
                    if (!self.wrapper.is(':hover')) {
                        self.close();
                    }
                }, 150);
            });
            
            // Dropdown events
            this.dropdown.on('click.bss', '.bs-server-select-item', function(e) {
                e.preventDefault();
                self.selectItem($(this));
            });
            
            this.dropdown.on('mouseenter.bss', '.bs-server-select-item', function() {
                self.setActiveItem($(this));
            });
            
            // Click outside to close
            $(document).on('click.bss', function(e) {
                if (!self.wrapper.is(e.target) && !self.wrapper.has(e.target).length) {
                    self.close();
                }
            });
        },

        handleInput: function(e) {
            const query = e.target.value.trim();
            
            if (this.searchTimeout) {
                clearTimeout(this.searchTimeout);
            }
            
            // Clear selection if input doesn't match
            if (this.getSelectedValue() && this.element.val() !== this.getSelectedText()) {
                this.clearSelection(false);
            }
            
            if (query.length >= this.options.minLength) {
                this.searchTimeout = setTimeout(() => {
                    this.search(query);
                }, this.options.debounceDelay);
            } else if (query.length === 0 && this.options.minLength === 0) {
                this.search('');
            } else {
                this.close();
            }
        },

        handleFocus: function(e) {
            if (this.dropdown.children().length > 0 && !this.isOpen) {
                this.open();
            }
        },

        handleKeydown: function(e) {
            if (!this.isOpen) {
                if (e.key === 'ArrowDown' || e.key === 'Enter') {
                    e.preventDefault();
                    this.open();
                }
                return;
            }
            
            const items = this.dropdown.find('.bs-server-select-item:visible');
            
            switch(e.key) {
                case 'ArrowDown':
                    e.preventDefault();
                    this.selectedIndex = Math.min(this.selectedIndex + 1, items.length - 1);
                    this.updateSelection();
                    break;
                    
                case 'ArrowUp':
                    e.preventDefault();
                    this.selectedIndex = Math.max(this.selectedIndex - 1, -1);
                    this.updateSelection();
                    break;
                    
                case 'Enter':
                    e.preventDefault();
                    if (this.selectedIndex >= 0 && items.eq(this.selectedIndex).length) {
                        this.selectItem(items.eq(this.selectedIndex));
                    }
                    break;
                    
                case 'Escape':
                    e.preventDefault();
                    this.close();
                    break;
                    
                case 'Tab':
                    this.close();
                    break;
            }
        },

        search: function(query) {
            const cacheKey = query.toLowerCase();
            
            // Check cache first
            if (this.options.cache && this.cache[cacheKey]) {
                this.renderResults(this.cache[cacheKey], query);
                this.open();
                return;
            }
            
            // Cancel previous request
            if (this.currentRequest) {
                this.currentRequest.abort();
            }
            
            this.showLoading();
            
            // Prepare request data
            const requestData = {};
            requestData[this.options.paramName] = query;
            
            // Make AJAX request
            const ajaxOptions = {
                url: this.options.apiUrl,
                method: this.options.method,
                data: requestData,
                dataType: 'json',
                headers: this.options.headers
            };
            
            // Call beforeSend callback
            if (typeof this.options.beforeSend === 'function') {
                this.options.beforeSend.call(this, ajaxOptions, query);
            }
            
            this.currentRequest = $.ajax(ajaxOptions);
            
            this.currentRequest
                .done((data) => {
                    let results = data;
                    
                    // Process results if callback provided
                    if (typeof this.options.processResults === 'function') {
                        results = this.options.processResults.call(this, data, query);
                    }
                    
                    // Limit results
                    if (this.options.maxResults > 0) {
                        results = results.slice(0, this.options.maxResults);
                    }
                    
                    // Cache results
                    if (this.options.cache) {
                        this.cache[cacheKey] = results;
                    }
                    
                    this.renderResults(results, query);
                    this.open();
                    
                    // Trigger event
                    this.element.trigger('bss:loaded', [results, query]);
                })
                .fail((xhr, status, error) => {
                    if (status !== 'abort') {
                        this.showError();
                        this.element.trigger('bss:error', [xhr, status, error]);
                    }
                })
                .always(() => {
                    this.currentRequest = null;
                });
        },

        renderResults: function(data, query) {
            this.dropdown.empty();
            this.selectedIndex = -1;
            
            if (!data || data.length === 0) {
                this.dropdown.html(`<div class="bs-server-select-no-results">${this.options.noResultsText}</div>`);
                return;
            }
            
            data.forEach((item, index) => {
                const element = this.createItemElement(item, index);
                this.dropdown.append(element);
            });
            
            // Auto-select first item if enabled
            if (this.options.autoSelectFirst && data.length > 0) {
                this.selectedIndex = 0;
                this.updateSelection();
            }
        },

        createItemElement: function(item, index) {
            const value = this.getItemValue(item);
            const text = this.getItemText(item);
            const subtitle = this.getItemSubtitle(item);
            
            if (typeof this.options.template === 'function') {
                return $(this.options.template.call(this, item, index));
            }
            
            const element = $('<div class="bs-server-select-item"></div>')
                .attr('data-value', value)
                .attr('data-text', text)
                .attr('data-index', index);
            
            const title = $('<div class="item-title"></div>').text(text);
            element.append(title);
            
            if (subtitle) {
                const subtitleEl = $('<div class="item-subtitle"></div>').text(subtitle);
                element.append(subtitleEl);
            }
            
            return element;
        },

        selectItem: function(item) {
            const value = item.data('value');
            const text = item.data('text');
            const index = item.data('index');
            
            this.element.val(text);
            
            if (this.hiddenInput) {
                this.hiddenInput.val(value);
            }
            
            this.close();
            
            // Trigger events
            this.element.trigger('change');
            this.element.trigger('bss:select', [value, text, item]);
            
            // Call onSelect callback
            if (typeof this.options.onSelect === 'function') {
                this.options.onSelect.call(this, value, text, item);
            }
        },

        setActiveItem: function(item) {
            this.selectedIndex = item.data('index') || item.index();
            this.updateSelection();
        },

        updateSelection: function() {
            const items = this.dropdown.find('.bs-server-select-item');
            items.removeClass('active');
            
            if (this.selectedIndex >= 0 && this.selectedIndex < items.length) {
                const activeItem = items.eq(this.selectedIndex);
                activeItem.addClass('active');
                
                // Scroll into view
                const dropdownHeight = this.dropdown.height();
                const itemTop = activeItem.position().top;
                const itemHeight = activeItem.outerHeight();
                
                if (itemTop + itemHeight > dropdownHeight) {
                    this.dropdown.scrollTop(this.dropdown.scrollTop() + itemTop + itemHeight - dropdownHeight);
                } else if (itemTop < 0) {
                    this.dropdown.scrollTop(this.dropdown.scrollTop() + itemTop);
                }
            }
        },

        showLoading: function() {
            this.dropdown.html(`
                <div class="bs-server-select-loading">
                    <div class="spinner-border" role="status">
                        <span class="visually-hidden">Loading...</span>
                    </div>
                    ${this.options.loadingText}
                </div>
            `);
            this.open();
        },

        showError: function() {
            this.dropdown.html(`<div class="bs-server-select-error">${this.options.errorText}</div>`);
            this.open();
        },

        open: function() {
            if (!this.isOpen) {
                this.dropdown.removeClass('hide').addClass('show');
                this.isOpen = true;
                this.element.trigger('bss:open', [this]);
            }
        },

        close: function() {
            if (this.isOpen) {
                this.dropdown.removeClass('show').addClass('hide');
                this.isOpen = false;
                this.selectedIndex = -1;
                this.element.trigger('bss:close', [this]);
            }
        },

        clearSelection: function(triggerEvent = true) {
            this.element.val('');
            
            if (this.hiddenInput) {
                this.hiddenInput.val('');
            }
            
            if (triggerEvent) {
                this.element.trigger('change');
                this.element.trigger('bss:clear', [this]);
                
                if (typeof this.options.onClear === 'function') {
                    this.options.onClear.call(this);
                }
            }
        },

        getSelectedValue: function() {
            return this.hiddenInput ? this.hiddenInput.val() : '';
        },

        getSelectedText: function() {
            return this.element.val();
        },

        getItemValue: function(item) {
            return this.options.valueKey ? item[this.options.valueKey] : item.id;
        },

        getItemText: function(item) {
            return this.options.displayKey ? item[this.options.displayKey] : item.name;
        },

        getItemSubtitle: function(item) {
            return this.options.subtitleKey ? item[this.options.subtitleKey] : null;
        },

        destroy: function() {
            // Remove events
            this.element.off('.bss');
            this.dropdown.off('.bss');
            $(document).off('.bss');
            
            // Remove DOM elements
            this.dropdown.remove();
            if (this.hiddenInput) {
                this.hiddenInput.remove();
            }
            
            // Unwrap element
            this.element.unwrap();
            
            // Remove data
            this.element.removeData('bss');
            
            // Trigger event
            this.element.trigger('bss:destroyed', [this]);
        },

        // Public methods
        setValue: function(value, text) {
            if (this.hiddenInput) {
                this.hiddenInput.val(value);
            }
            this.element.val(text || '');
        },

        clear: function() {
            this.clearSelection();
        },

        refresh: function() {
            this.cache = {};
            const query = this.element.val().trim();
            if (query.length >= this.options.minLength) {
                this.search(query);
            }
        }
    };

    // jQuery plugin definition
    $.fn.bootstrapServerSelect = function(option) {
        const args = Array.prototype.slice.call(arguments, 1);
        
        return this.each(function() {
            const $this = $(this);
            let data = $this.data('bss');
            const options = typeof option === 'object' && option;
            
            if (!data) {
                data = new BootstrapServerSelect(this, options);
                $this.data('bss', data);
            }
            
            if (typeof option === 'string') {
                if (typeof data[option] === 'function') {
                    data[option].apply(data, args);
                } else {
                    throw new Error(`Unknown method: ${option}`);
                }
            }
        });
    };

    // Constructor reference
    $.fn.bootstrapServerSelect.Constructor = BootstrapServerSelect;

    // Default options
    $.fn.bootstrapServerSelect.defaults = DEFAULTS;

    // Auto-initialize
    $(document).ready(function() {
        $('[data-toggle="server-select"]').bootstrapServerSelect();
    });

    return BootstrapServerSelect;
}));
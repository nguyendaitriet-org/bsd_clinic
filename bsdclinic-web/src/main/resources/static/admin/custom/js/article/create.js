import {App, SweetAlert, DebounceUtil} from "/common/js/app.js";
import {RequestHeader} from "/common/js/constant.js";
import {CategoryComponent} from "/admin/custom/js/category/component.js";
import {FormHandler} from "/common/js/form.js";

export const ArticleCreation = (function () {
    const module = {
        articleEditorSelector: $('#article-editor'),
        saveArticleButtonSelector: $('#submit-btn')
    };

    module.init = () => {
        initArticleEditor();
        handleSaveArticleButton();
    }

    const initArticleEditor = () => {
        module.articleEditor = new Quill('#article-editor', {
            theme: 'snow',
            modules: {
                toolbar: [
                    ['bold', 'italic', 'underline', 'strike'],
                    ['blockquote', 'code-block'],
                    ['link', 'image', 'video', 'formula'],
                    [{ 'header': 1 }, { 'header': 2 }],
                    [{ 'list': 'ordered'}, { 'list': 'bullet' }, { 'list': 'check' }],
                    [{ 'script': 'sub'}, { 'script': 'super' }],
                    [{ 'indent': '-1'}, { 'indent': '+1' }],
                    [{ 'direction': 'rtl' }],
                    [{ 'size': ['small', false, 'large', 'huge'] }],
                    [{ 'header': [1, 2, 3, 4, 5, 6, false] }],
                    [{ 'color': [] }, { 'background': [] }],
                    [{ 'font': [] }],
                    [{ 'align': [] }],
                    ['clean']
                ]
            }
        });
    }

    const handleSaveArticleButton = () => {
        module.saveArticleButtonSelector.on('click', function () {
            console.log(module.articleEditor.root.innerHTML)
        });
    }

    return module;
})();
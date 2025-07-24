// Smooth scrolling for anchor links
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();
        const target = document.querySelector(this.getAttribute('href'));
        if (target) {
            target.scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });
        }
    });
});

// Add animation classes when elements come into view
const observerOptions = {
    threshold: 0.1,
    rootMargin: '0px 0px -100px 0px'
};

const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            entry.target.classList.add('animate-in');
        }
    });
}, observerOptions);

// Observe all animated elements
document.addEventListener('DOMContentLoaded', () => {
    const animatedElements = document.querySelectorAll('.tip-card, .step-item, .ingredient-card, .timeline-item, .key-point, .factor-card, .care-item, .advice-item');
    animatedElements.forEach(el => {
        observer.observe(el);
    });

    // Add CSS for animation
    const style = document.createElement('style');
    style.textContent = `
        .tip-card,
        .step-item,
        .ingredient-card,
        .timeline-item,
        .key-point,
        .factor-card,
        .care-item,
        .advice-item {
            opacity: 0;
            transform: translateY(20px);
            transition: all 0.6s ease;
        }
        
        .animate-in {
            opacity: 1 !important;
            transform: translateY(0) !important;
        }
        
        .loaded {
            opacity: 1;
            transition: opacity 0.3s ease;
        }
        
        img {
            opacity: 0;
            transition: opacity 0.3s ease;
        }
        
        img.loaded {
            opacity: 1;
        }
    `;
    document.head.appendChild(style);
});

// Add loading animation to images
document.addEventListener('DOMContentLoaded', () => {
    const images = document.querySelectorAll('img');
    images.forEach(img => {
        // Set initial opacity
        img.style.opacity = '0';

        img.addEventListener('load', () => {
            img.classList.add('loaded');
            img.style.opacity = '1';
        });

        if (img.complete) {
            img.classList.add('loaded');
            img.style.opacity = '1';
        }
    });
});

// Add hover effects to buttons
document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.btn').forEach(btn => {
        btn.addEventListener('mouseenter', () => {
            if (!btn.style.transform.includes('translateY')) {
                btn.style.transform = 'translateY(-2px)';
            }
        });

        btn.addEventListener('mouseleave', () => {
            btn.style.transform = 'translateY(0)';
        });
    });
});

// Add click tracking for CTA buttons (for analytics)
document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.btn').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const buttonText = btn.textContent.trim();
            console.log(`Button clicked: ${buttonText}`);

            // Add your analytics tracking code here
            // Example: gtag('event', 'click', { button_text: buttonText });
        });
    });
});

// Add scroll progress indicator
const createScrollProgress = () => {
    const progressBar = document.createElement('div');
    progressBar.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        width: 0%;
        height: 3px;
        background: linear-gradient(90deg, #4facfe, #00f2fe);
        z-index: 9999;
        border-radius: 0 2px 2px 0;
        transition: width 0.1s ease;
    `;
    document.body.appendChild(progressBar);

    window.addEventListener('scroll', () => {
        const windowHeight = document.documentElement.scrollHeight - window.innerHeight;
        const scrolled = (window.scrollY / windowHeight) * 100;
        progressBar.style.width = scrolled + '%';
    });
};

// Initialize scroll progress
document.addEventListener('DOMContentLoaded', createScrollProgress);

// Add back to top button
const createBackToTop = () => {
    const backToTop = document.createElement('button');
    backToTop.innerHTML = '<i class="fas fa-arrow-up"></i>';
    backToTop.setAttribute('aria-label', 'Về đầu trang');
    backToTop.style.cssText = `
        position: fixed;
        bottom: 30px;
        right: 30px;
        width: 50px;
        height: 50px;
        border-radius: 50%;
        background: linear-gradient(135deg, #4facfe, #00f2fe);
        color: white;
        border: none;
        cursor: pointer;
        opacity: 0;
        transform: translateY(20px);
        transition: all 0.3s ease;
        z-index: 1000;
        box-shadow: 0 4px 15px rgba(79, 172, 254, 0.3);
        font-size: 1.2rem;
        display: flex;
        align-items: center;
        justify-content: center;
    `;

    backToTop.addEventListener('click', () => {
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    });

    backToTop.addEventListener('mouseenter', () => {
        backToTop.style.transform = backToTop.style.transform.includes('translateY(0)')
            ? 'translateY(0) scale(1.1)'
            : 'translateY(20px) scale(1.1)';
    });

    backToTop.addEventListener('mouseleave', () => {
        backToTop.style.transform = window.scrollY > 300
            ? 'translateY(0) scale(1)'
            : 'translateY(20px) scale(1)';
    });

    window.addEventListener('scroll', () => {
        if (window.scrollY > 300) {
            backToTop.style.opacity = '1';
            backToTop.style.transform = 'translateY(0)';
        } else {
            backToTop.style.opacity = '0';
            backToTop.style.transform = 'translateY(20px)';
        }
    });

    document.body.appendChild(backToTop);
};

// Initialize back to top button
document.addEventListener('DOMContentLoaded', createBackToTop);

// Add reading time calculator
const calculateReadingTime = () => {
    const article = document.querySelector('#content');
    if (!article) return;

    const text = article.textContent || article.innerText || '';
    const wordCount = text.trim().split(/\s+/).length;
    const readingTime = Math.ceil(wordCount / 200); // Average 200 words per minute

    // Add reading time to hero section
    const heroSection = document.querySelector('.hero-section .author-info small');
    if (heroSection) {
        const currentContent = heroSection.innerHTML;
        if (!currentContent.includes('phút đọc')) {
            heroSection.innerHTML = currentContent + ` | <i class="fas fa-clock me-1"></i>${readingTime} phút đọc`;
        }
    }
};

// Initialize reading time
document.addEventListener('DOMContentLoaded', calculateReadingTime);

// Add lazy loading for images
const lazyLoadImages = () => {
    const images = document.querySelectorAll('img[src]');
    if (images.length === 0) return;

    const imageObserver = new IntersectionObserver((entries, observer) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                const img = entry.target;

                if (!img.classList.contains('loaded')) {
                    img.style.transition = 'opacity 0.3s ease';

                    img.onload = () => {
                        img.classList.add('loaded');
                        img.style.opacity = '1';
                    };

                    if (img.complete) {
                        img.classList.add('loaded');
                        img.style.opacity = '1';
                    }
                }

                observer.unobserve(img);
            }
        });
    });

    images.forEach(img => imageObserver.observe(img));
};

// Initialize lazy loading
document.addEventListener('DOMContentLoaded', lazyLoadImages);

// Initialize share buttons
document.addEventListener('DOMContentLoaded', addShareButtons);

// Add error handling for missing elements
document.addEventListener('DOMContentLoaded', () => {
    // Check if all required elements exist
    const requiredElements = [
        '.hero-section',
        '#content',
        '.conclusion-section'
    ];

    requiredElements.forEach(selector => {
        const element = document.querySelector(selector);
        if (!element) {
            console.warn(`Element ${selector} not found`);
        }
    });
});

// Smooth scroll polyfill for older browsers
if (!('scrollBehavior' in document.documentElement.style)) {
    const smoothScrollPolyfill = () => {
        const links = document.querySelectorAll('a[href^="#"]');
        links.forEach(link => {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                const target = document.querySelector(link.getAttribute('href'));
                if (target) {
                    const targetPosition = target.offsetTop;
                    const startPosition = window.pageYOffset;
                    const distance = targetPosition - startPosition;
                    const duration = 1000;
                    let start = null;

                    const step = (timestamp) => {
                        if (!start) start = timestamp;
                        const progress = timestamp - start;
                        const ease = easeInOutCubic(progress / duration);
                        window.scrollTo(0, startPosition + distance * ease);
                        if (progress < duration) {
                            window.requestAnimationFrame(step);
                        }
                    };

                    window.requestAnimationFrame(step);
                }
            });
        });
    };

    const easeInOutCubic = (t) => {
        return t < 0.5 ? 4 * t * t * t : (t - 1) * (2 * t - 2) * (2 * t - 2) + 1;
    };

    document.addEventListener('DOMContentLoaded', smoothScrollPolyfill);
}
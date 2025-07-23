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

// Fade in animation on scroll
const observerOptions = {
    threshold: 0.1,
    rootMargin: '0px 0px -50px 0px'
};

const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            entry.target.classList.add('visible');
        }
    });
}, observerOptions);

document.querySelectorAll('.fade-in').forEach(el => {
    observer.observe(el);
});

// Navbar background on scroll
window.addEventListener('scroll', () => {
    const navbar = document.querySelector('.navbar');
    if (window.scrollY > 50) {
        navbar.style.background = 'rgba(255,255,255,0.98)';
    } else {
        navbar.style.background = 'rgba(255,255,255,0.95)';
    }
});

// Contact buttons functionality
document.addEventListener('DOMContentLoaded', function() {
    // Phone call buttons
    const phoneButtons = document.querySelectorAll('button:contains("Gọi ngay"), button:contains("0652478964")');
    phoneButtons.forEach(button => {
        if (button.textContent.includes('Gọi ngay') || button.textContent.includes('0652478964')) {
            button.addEventListener('click', () => {
                window.location.href = 'tel:0652478964';
            });
        }
    });

    // Website buttons
    const websiteButtons = document.querySelectorAll('button:contains("bsdclinic.site")');
    websiteButtons.forEach(button => {
        if (button.textContent.includes('bsdclinic.site')) {
            button.addEventListener('click', () => {
                window.open('https://bsdclinic.site', '_blank');
            });
        }
    });

    // Zalo buttons
    const zaloButtons = document.querySelectorAll('button:contains("Chat Zalo")');
    zaloButtons.forEach(button => {
        if (button.textContent.includes('Chat Zalo')) {
            button.addEventListener('click', () => {
                window.open('https://zalo.me/0652478964', '_blank');
            });
        }
    });
});
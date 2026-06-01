function createParticles() {
    const container = document.getElementById('particles');
    if (!container) return;
    
    const particleCount = 50;
    
    for (let i = 0; i < particleCount; i++) {
        const particle = document.createElement('div');
        particle.className = 'particle';
        particle.style.left = Math.random() * 100 + '%';
        particle.style.top = Math.random() * 100 + '%';
        particle.style.animationDelay = Math.random() * 15 + 's';
        particle.style.animationDuration = (10 + Math.random() * 10) + 's';
        container.appendChild(particle);
    }
}

function addAnimations() {
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.opacity = '1';
                entry.target.style.transform = 'translateY(0)';
            }
        });
    }, { threshold: 0.1 });

    document.querySelectorAll('.feature-card, .flight-card, .booking-card').forEach(card => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(20px)';
        card.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
        observer.observe(card);
    });
}

function setupAutocomplete(inputId, suggestionsId) {
    const input = document.getElementById(inputId);
    const suggestions = document.getElementById(suggestionsId);
    
    if (!input || !suggestions) return;
    
    let debounceTimer;
    
    input.addEventListener('input', () => {
        clearTimeout(debounceTimer);
        const query = input.value.trim();
        
        if (query.length >= 2) {
            debounceTimer = setTimeout(() => {
                fetch(`/api/cities?query=${encodeURIComponent(query)}`)
                    .then(response => response.json())
                    .then(cities => {
                        suggestions.innerHTML = '';
                        if (cities.length > 0) {
                            cities.forEach(city => {
                                const suggestion = document.createElement('div');
                                suggestion.className = 'autocomplete-suggestion';
                                suggestion.textContent = city;
                                suggestion.addEventListener('click', () => {
                                    input.value = city;
                                    suggestions.classList.remove('active');
                                    suggestions.innerHTML = '';
                                });
                                suggestions.appendChild(suggestion);
                            });
                            suggestions.classList.add('active');
                        } else {
                            suggestions.classList.remove('active');
                        }
                    })
                    .catch(error => {
                        console.error('Error fetching cities:', error);
                    });
            }, 300);
        } else {
            suggestions.classList.remove('active');
        }
    });
    
    // Close suggestions when clicking outside
    document.addEventListener('click', (e) => {
        if (!input.contains(e.target) && !suggestions.contains(e.target)) {
            suggestions.classList.remove('active');
        }
    });
}

document.addEventListener('DOMContentLoaded', () => {
    createParticles();
    addAnimations();
    
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            document.querySelector(this.getAttribute('href')).scrollIntoView({
                behavior: 'smooth'
            });
        });
    });
    
    // Setup autocomplete for both city fields
    setupAutocomplete('departure', 'departure-suggestions');
    setupAutocomplete('arrival', 'arrival-suggestions');
});

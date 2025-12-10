// asset/js/main.js

document.addEventListener('DOMContentLoaded', function() {
    
    // --- 1. 모바일 메뉴 패널 열고 닫기 ---
    const hamburgerBtn = document.getElementById('hamburger-btn');
    const mobileMenu = document.getElementById('mobile-menu');
    const closeMenuBtn = document.getElementById('close-menu-btn');

    if (hamburgerBtn && mobileMenu && closeMenuBtn) {
        hamburgerBtn.addEventListener('click', function() {
            mobileMenu.classList.add('active');
        });
        closeMenuBtn.addEventListener('click', function() {
            mobileMenu.classList.remove('active');
        });
    }

    // --- 2. 모바일 메뉴 안의 드롭다운 토글 ---
    document.querySelectorAll('.mobile-nav-links .dropdown-toggle').forEach(toggle => {
        toggle.addEventListener('click', function(event) {
            event.preventDefault(); 
            const parentItem = this.closest('.has-dropdown');
            const subMenu = parentItem.querySelector('.mobile-sub-menu');
            // 클릭된 메뉴가 이전에 이미 열려 있었는지 상태 확인
            const wasActive = parentItem.classList.contains('active'); 

            // 먼저 모든 드롭다운을 닫습니다. (클릭된 메뉴 포함)
            document.querySelectorAll('.mobile-nav-links .has-dropdown.active').forEach(openItem => {
                openItem.classList.remove('active'); 
                const openSubMenu = openItem.querySelector('.mobile-sub-menu');
                if (openSubMenu) {
                    openSubMenu.style.display = 'none';
                }
            });

            // 만약 클릭된 메뉴가 이전에 닫혀 있었다면, 다시 열어줍니다.
            if (!wasActive) { 
                parentItem.classList.add('active');
                if (subMenu) {
                    subMenu.style.display = 'block';
                }
            }
            // (만약 이전에 열려 있었다면, 위에서 이미 닫혔으므로 아무것도 안 함)
        });
    });

}); // DOMContentLoaded

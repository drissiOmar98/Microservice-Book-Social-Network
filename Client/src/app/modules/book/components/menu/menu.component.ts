import {AfterViewInit, Component, ElementRef, OnDestroy, OnInit, Renderer2} from '@angular/core';



@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss'
})
export class MenuComponent implements OnInit    {

  ngOnInit(): void {
    const linkColor = document.querySelectorAll('.nav-link');
    linkColor.forEach(link => {
      const icon = link.querySelector('i');
      if (window.location.href.endsWith(link.getAttribute('href') || '')) {
        link.classList.add('active');
        if (icon) {
          icon.classList.add('active');
        }
      }
      link.addEventListener('click', () => {
        linkColor.forEach(l => {
          l.classList.remove('active');
          const lIcon = l.querySelector('i');
          if (lIcon) {
            lIcon.classList.remove('active');
          }
        });
        link.classList.add('active');
        if (icon) {
          icon.classList.add('active');
        }
      });
    });
  }




  logout() {

  }


}

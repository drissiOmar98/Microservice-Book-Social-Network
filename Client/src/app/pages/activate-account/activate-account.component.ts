import {Component, OnDestroy, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/auth-service/services/authentication.service";

@Component({
  selector: 'app-activate-account',
  templateUrl: './activate-account.component.html',
  styleUrl: './activate-account.component.scss'
})
export class ActivateAccountComponent implements OnInit,OnDestroy {

  message = '';
  isOkay = true;
  submitted = false;

  constructor(
    private router: Router,
    private authService: AuthenticationService
  ) {}

  ngOnInit(): void {
    this.loadScript('assets/js/custom/authentication/sign-in/two-factor.js');
  }

  ngOnDestroy(): void {
    this.removeScript('assets/js/custom/authentication/sign-in/two-factor.js');
  }

  loadScript(url: string): void {
    const script = document.createElement('script');
    script.src = url;
    script.id = 'custom-script'; // Adding an ID to identify the script
    document.body.appendChild(script);
  }

  removeScript(url: string): void {
    const scriptElement = document.getElementById('custom-script');
    if (scriptElement) {
      document.body.removeChild(scriptElement);
    }
  }

  private confirmAccount(token: string) {
    this.authService.confirm({
      token
    }).subscribe({
      next: () => {
        this.message = 'Your account has been successfully activated.\nNow you can proceed to login';
        this.submitted = true;
      },
      error: () => {
        this.message = 'Token has been expired or invalid';
        this.submitted = true;
        this.isOkay = false;
      }
    });
  }

  onCodeCompleted($event: string) {
    this.confirmAccount($event);
  }


  redirectToLogin() {
    this.router.navigate(['login']);
  }
}

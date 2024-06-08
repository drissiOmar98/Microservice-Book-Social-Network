import {Component, OnDestroy, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/auth-service/services/authentication.service";
import {TokenService} from "../../services/token-service/token.service";
import {RegistrationRequest} from "../../services/auth-service/models/registration-request";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent implements OnInit,OnDestroy{

  constructor(
    private router: Router,
    private authService: AuthenticationService,
    private tokenService: TokenService

  ) {
  }

  ngOnInit(): void {
    this.loadScript('assets/js/custom/authentication/sign-up/general.js');
  }

  ngOnDestroy(): void {
    this.removeScript('assets/js/custom/authentication/sign-up/general.js');
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

  login() {
    this.router.navigate(['login']);
  }

  registerRequest: RegistrationRequest = {firstname: '', lastname: '',email: '', password: ''};
  errorMsg: Array<string> = [];



  register() {
    this.errorMsg = [];
    this.authService.register({
      body: this.registerRequest
    })
      .subscribe({

        next: () => {
          console.log(this.registerRequest)
          this.router.navigate(['activate-account']);
        },
        error: (err) => {
          console.log(this.registerRequest)
          this.errorMsg = err.error.validationErrors;
        }
      });
  }
}

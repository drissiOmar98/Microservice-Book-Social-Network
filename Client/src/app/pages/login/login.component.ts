import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthenticationRequest} from "../../services/auth-service/models/authentication-request";
import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/auth-service/services/authentication.service";
import {TokenService} from "../../services/token-service/token.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit,OnDestroy{

  constructor(
    private router: Router,
    private authService: AuthenticationService,
    private tokenService: TokenService

  ) {
  }

  ngOnInit(): void {
    this.loadScript('assets/js/custom/authentication/sign-in/general.js');
  }

  ngOnDestroy(): void {
    this.removeScript('assets/js/custom/authentication/sign-in/general.js');
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

  authRequest: AuthenticationRequest = {email: '', password: ''};
  errorMsg: Array<string> = [];



  login() {
    this.errorMsg = [];
    this.authService.authenticate({
      body: this.authRequest
    }).subscribe({
      next: (res) => {
        this.tokenService.token = res.token as string;
        this.router.navigate(['books']);
      },
      error: (err) => {
        console.log(err);
        if (err.error.validationErrors) {
          this.errorMsg = err.error.validationErrors;
        } else {
          this.errorMsg.push(err.error.error);
        }
      }
    });
  }

  register() {
    this.router.navigate(['register']);
  }
}



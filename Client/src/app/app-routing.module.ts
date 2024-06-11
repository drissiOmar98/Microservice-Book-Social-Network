import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./pages/login/login.component";
import {RegisterComponent} from "./pages/register/register.component";
import {ActivateAccountComponent} from "./pages/activate-account/activate-account.component";

const routes: Routes = [

  {
    path: "",
    redirectTo: "login",
    pathMatch: "full",
  },

  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: "activate-account",
    component: ActivateAccountComponent
  },
  {
    path: "books",
    loadChildren: () => import('./modules/book/book.module').then( m => m.BookModule)
  }


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

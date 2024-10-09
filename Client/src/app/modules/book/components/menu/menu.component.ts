import {AfterViewInit, Component, ElementRef, OnDestroy, OnInit, Renderer2} from '@angular/core';
import {ToastrService} from "ngx-toastr";
import {TokenService} from "../../../../services/token-service/token.service";
import {document} from "ngx-bootstrap/utils";
import {JwtHelperService} from "@auth0/angular-jwt";
import * as Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import {Notification} from "./notification";



@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss'
})
/**
 * MenuComponent manages the navigation and real-time notification system of the application.
 * It connects to a WebSocket for notifications and updates the user interface accordingly.
 */
export class MenuComponent implements OnInit , OnDestroy   {


  constructor(
    private tokenService: TokenService,
    private toastService: ToastrService
  ) {
    this.fullName = this.getFullNameFromToken();
  }

  // The full name of the currently logged-in user, extracted from the JWT token.
  fullName: string | null;

  // WebSocket client for real-time notifications.
  socketClient: any = null;

  // Subscription object for the notification WebSocket.
  private notificationSubscription: any;
  // Count of unread notifications, displayed in the UI.
  unreadNotificationsCount = 0;
  // Array holding notifications received from the WebSocket.
  notifications: Array<Notification> = [];



  ngOnInit(): void {
    //this.navigationHandler();
    // Initialize WebSocket connection if token is valid and extract user ID for the connection.
    // Check if the token is valid using TokenService
    if (this.tokenService.isTokenValid()) {
      const userId = this.getUserIdFromToken(); // A method to extract user ID from the token

      if (userId) {
        const ws = new SockJS('http://localhost:8070/api/v1/ws');
        this.socketClient = Stomp.over(ws);
        this.socketClient.connect(
          { 'Authorization': 'Bearer ' + this.tokenService.token },
          () => {
            this.notificationSubscription = this.socketClient.subscribe(
              `/user/${userId}/notifications`,
              (message: any) => {
                const notification = JSON.parse(message.body);
                if (notification) {
                  this.notifications.unshift(notification);
                  this.handleNotification(notification); // New method to handle notifications
                  this.unreadNotificationsCount++;
                }
              },
              () => {
                console.error('Error while connecting to webSocket');
              }
            );
          }
        );
      }
    }
    const linkColor = document.querySelectorAll('.nav-link');
    linkColor.forEach((link:any) => {
      const icon = link.querySelector('i');
      if (window.location.href.endsWith(link.getAttribute('href') || '')) {
        link.classList.add('active');
        if (icon) {
          icon.classList.add('active');
        }
      }
      link.addEventListener('click', () => {
        linkColor.forEach((l: any)=> {
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


  ngOnDestroy(): void {
    if (this.socketClient !== null) {
      this.socketClient.disconnect();
      this.notificationSubscription.unsubscribe();
      this.socketClient = null;
    }
  }


  /**
   * Displays a toast notification based on the notification's status.
   *
   * @param notification - The notification object containing status, message, and book title.
   */
  private handleNotification(notification: any): void {
    switch (notification.status) {
      case 'BORROWED':
        this.toastService.info(notification.message, notification.bookTitle);
        break;
      case 'RETURNED':
        this.toastService.warning(notification.message, notification.bookTitle);
        break;
      case 'RETURN_APPROVED':
        this.toastService.success(notification.message, notification.bookTitle);
        break;
    }
  }

  /**
   * Logs out the user by clearing the token from local storage and refreshing the page.
   */
  logout() {
    localStorage.removeItem('token');
    window.location.reload();
  }


  /**
   * Extracts the user ID from the JWT token.
   *
   * @returns The user ID if present in the token, otherwise null.
   */
  private getUserIdFromToken(): string | null {
    const token = this.tokenService.token;
    if (!token) {
      return null;
    }

    const jwtHelper = new JwtHelperService();
    const decodedToken = jwtHelper.decodeToken(token);
    return decodedToken?.userId?.toString() || null; // Convert userId to string if necessary // Assuming 'sub' is the user ID
  }

  /**
   * Extracts the full name from the JWT token using a helper service.
   *
   * @returns The full name if present in the token, otherwise null.
   */
  private getFullNameFromToken(): string | null {
    const token = this.tokenService.token; // Assuming tokenService holds the token
    if (!token) {
      return null;
    }

    const jwtHelper = new JwtHelperService();
    const decodedToken = jwtHelper.decodeToken(token);
    return decodedToken?.fullName || null; // Extract fullName
  }


}

import {Component} from '@angular/core';
import {SocialAuthService} from "angularx-social-login";
import { FacebookLoginProvider, GoogleLoginProvider } from "angularx-social-login";
import {Observable} from "rxjs";
import {User} from "./models/user";
import {UserService} from "./services/user.service";
import {Location} from '@angular/common';

declare var $: any;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'TennisPadel Sports Centre';
  description = 'Your best services for your best sports!';

  userLogged!: Observable<User>;

  loggedIn = false;
  usernameSetted = false;
  //SPIEGAZIONE: sono 2 flag che operano in situazioni diverse ma riguardano sempre l'utente loggato, per evitare una subscribe dentro una subscribe.
  //flag loggedIn: verrà settato a TRUE dopo il login. Nel momento in cui è settato verranno mostrati i main buttons soltanto (tutti i servizi course, reservation ecc.)
  //flag usernameSetted: verrà settato a true dopo che l'interazione col server avrà avuto successo. Verrà settato con la setUsername chiamata dai main buttons

  constructor(private authService: SocialAuthService, private userService: UserService, private location: Location) {
    //riporta al root se non si è collegati. Evita problemi con il router outlet
    //SPIEGAZIONE: se rimani nel path ad esempio create course e non sei già loggato, tu sei già li ma prima devi loggarti.
    // Dopo che ti colleghi e riclicchi su quel percorso c'è l'errore, perchè stai andando in un percorso già attivato
    //if (!this.userService.getUserLogged())
      //this.location.go('/');
  }

  ngOnInit() {
    this.authService.authState.subscribe((socialuser) => {
      this.loggedIn = (socialuser != null);
      if (this.loggedIn)
        this.userLogged = this.userService.loginUser(socialuser.email, socialuser.firstName, socialuser.lastName);
    });
  }

  signInWithGoogle(): void {
    this.authService.signIn(GoogleLoginProvider.PROVIDER_ID);
  }

  signInWithFB(): void {
    this.authService.signIn(FacebookLoginProvider.PROVIDER_ID);
  }

  signOut(): void {
    this.authService.signOut();
    this.userService.setUserLogged('');
    this.loggedIn = false;
  }

  refreshToken(): void {
    this.authService.refreshAuthToken(GoogleLoginProvider.PROVIDER_ID);
  }

  setUsername(): void {
    if (!this.usernameSetted)
      this.userLogged.subscribe(data => {
        this.userService.setUserLogged(data.username);
        this.userService.setRoleUserLogged(data.typeuser);
        this.userService.setIdUserLogged(data.id);
        this.usernameSetted = true;
      });
  }
}

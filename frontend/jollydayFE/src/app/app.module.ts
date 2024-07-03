import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HTTP_INTERCEPTORS, provideHttpClient, withFetch, withInterceptorsFromDi } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { UsersListComponent } from './users-list/users-list.component';
import { CreateUserComponent } from './create-user/create-user.component';
import { UpdateUserComponent } from './update-user/update-user.component';
import { ViewUserComponent } from './view-user/view-user.component';
import { SignupComponent } from './signup/signup.component';
import { LoginComponent } from './login/login.component';
import { LogoutComponent } from './logout/logout.component';
import { JwtInterceptor } from './jwt-interceptor';
import { LoginService } from './login.service';
import { GroupListComponent } from './group-list/group-list.component';
import { GroupUsersListComponent } from './group-users-list/group-users-list.component';
import { EventListComponent } from './event-list/event-list.component';
import { AddGroupComponent } from './add-group/add-group.component';

@NgModule({
  declarations: [
    AppComponent,
    UsersListComponent,
    CreateUserComponent,
    UpdateUserComponent,
    ViewUserComponent,
    SignupComponent,
    LoginComponent,
    LogoutComponent,
    GroupListComponent,
    GroupUsersListComponent,
    EventListComponent,
    AddGroupComponent
  ],
  imports: [
    FormsModule,
    BrowserModule,
    AppRoutingModule
  ],
  providers: [
    provideClientHydration(),
    provideHttpClient(
      withInterceptorsFromDi(),
      withFetch()
    ),
    LoginService,
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

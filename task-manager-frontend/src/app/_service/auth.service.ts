import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { EnvService } from './env.service';
import { User } from '../_model/user';
import { APIResponseDTO, AuthenticationResponseDTO } from '../_model/dto';
import { UtilMethods } from '../util/util';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private url: string = `${this.envService.getApiUrl}/auth`;

  constructor(
    private http: HttpClient,
    private router: Router,
    private envService: EnvService
  ) { }

  login(email: string, password: string) {
    let user: User = new User(); 
    user.email = email;
    user.password = password;
    
    return this.http.post<APIResponseDTO<AuthenticationResponseDTO>>(`${this.url}/login`, user);
  }

  isLogged() {
    let token = UtilMethods.getInstance().getJwtToken();
    return token != null;
  }

  logout() {
    this.http.get(`${this.url}/logout`)
      .subscribe(() => {
        sessionStorage.clear();
        this.router.navigate(['login']);
      })

  }
}

import { ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideClientHydration } from '@angular/platform-browser';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { JwtModule } from '@auth0/angular-jwt';
import { EnvService } from './_service/env.service';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

const envService = new EnvService();  

export function jwtTokenGetter(): string {
  return sessionStorage.getItem(envService.getTokenName) || '';
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes), 
    provideAnimationsAsync(),
    provideClientHydration(),
    provideHttpClient(withInterceptorsFromDi()),
    importProvidersFrom(
      JwtModule.forRoot({
        config: {
          tokenGetter: jwtTokenGetter,
          allowedDomains: envService.getDomains,
          disallowedRoutes: [
            `${envService.getApiUrl}/auth/login`,
            `${envService.getApiUrl}/auth/register`
          ]
        }
      })
    ) 
  ]
};

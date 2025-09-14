import { Injectable, Inject, PLATFORM_ID, Optional } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class EnvService {

  private production: boolean;
  private apiUrl: string;
  private tokenName: string;
  private domains: string[];

  constructor(@Optional() @Inject(PLATFORM_ID) private platformId?: Object) { 
    let env: any = {};
    
    // Check if we're in a browser environment
    if (this.platformId ? isPlatformBrowser(this.platformId) : typeof window !== 'undefined') {
      env = (window as any).__env || {};
    }

    this.production = env.production || false;
    this.apiUrl = env.apiUrl || 'http://localhost:8080/rest/api/v1';
    this.tokenName = env.token_name || 'access_token';
    this.domains = env.domains || ['localhost:8080'];
  }

  get isProduction(): boolean {
    return this.production;
  }

  get getApiUrl(): string {
    return this.apiUrl;
  }

  get getTokenName(): string {
    return this.tokenName;
  }

  get getDomains(): string[] {
    return this.domains;
  }
}

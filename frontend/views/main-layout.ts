import '@vaadin/app-layout';
import { AppLayout } from '@vaadin/app-layout';
import '@vaadin/app-layout/vaadin-drawer-toggle';
import '@vaadin/avatar/vaadin-avatar';
import '@vaadin/context-menu';
import '@vaadin/tabs';
import '@vaadin/tabs/vaadin-tab';
import { html } from 'lit';
import { customElement } from 'lit/decorators.js';
import '../components/app-nav/app-nav';
import '../components/app-nav/app-nav-item';
import { router } from '../index';
import { appStore } from '../stores/app-store';
import { Layout } from './view';

interface RouteInfo {
  path: string;
  title: string;
  icon: string;
}

@customElement('main-layout')
export class MainLayout extends Layout {
  render() {
    return html`
      <vaadin-app-layout primary-section="drawer">
        <header class="view-header" slot="navbar">
          <vaadin-drawer-toggle aria-label="Menu toggle" class="view-toggle" theme="contrast"></vaadin-drawer-toggle>
          <h2 class="view-title">${appStore.currentViewTitle}</h2>
        </header>
        <section class="drawer-section" slot="drawer">
          <h1 class="app-name">${appStore.applicationName}</h1>
          <app-nav class="app-nav" aria-label="${appStore.applicationName}">
            ${this.getMenuRoutes().map(
              (viewRoute) => html`
                <app-nav-item path=${router.urlForPath(viewRoute.path)}>
                  <span class="${viewRoute.icon} nav-item-icon" slot="prefix" aria-hidden="true"></span>
                  ${viewRoute.title}
                </app-nav-item>
              `
            )}
          </app-nav>
          <footer class="app-nav-footer"></footer>
        </section>
        <slot></slot>
      </vaadin-app-layout>
    `;
  }

  connectedCallback() {
    super.connectedCallback();
    this.classList.add('block', 'h-full');
    this.reaction(
      () => appStore.location,
      () => {
        AppLayout.dispatchCloseOverlayDrawerEvent();
      }
    );
  }

  private getMenuRoutes(): RouteInfo[] {
    return [
      {
        path: 'updates',
        title: 'Updates',
        icon: 'la la-columns',
      },

      {
        path: 'updates-hilla',
        title: 'Updates Hilla',
        icon: 'la la-columns',
      },
    ];
  }
}

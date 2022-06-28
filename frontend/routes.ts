import { Flow } from '@vaadin/flow-frontend';
import { Route } from '@vaadin/router';
import './views/main-layout';

const { serverSideRoutes } = new Flow({
  imports: () => import('../target/frontend/generated-flow-imports'),
});

export type ViewRoute = Route & {
  title?: string;
  icon?: string;
  children?: ViewRoute[];
};

export const views: ViewRoute[] = [
  // place routes below (more info https://hilla.dev/docs/routing)
  {
    path: 'updates-hilla',
    component: 'updates-hilla-view',
    icon: 'la la-columns',
    title: 'Tesla Software Updates',
    action: async (_context, _command) => {
      await import('./views/updateshilla/updates-hilla-view');
      return;
    },
  },
  {
    path: 'updates-hilla-solution',
    component: 'updates-hilla-view-solution',
    icon: 'la la-columns',
    title: 'Tesla Software Updates',
    action: async (_context, _command) => {
      await import('./views/updateshilla/updates-hilla-view-solution');
      return;
    },
  },
];
export const routes: ViewRoute[] = [
  {
    path: '',
    component: 'main-layout',
    children: [
      ...views,
      // for server-side, the next magic line sends all unmatched routes:
      ...serverSideRoutes, // IMPORTANT: this must be the last entry in the array
    ],
  },
];

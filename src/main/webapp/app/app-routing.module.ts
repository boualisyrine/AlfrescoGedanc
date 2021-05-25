import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { errorRoute } from './layouts/error/error.route';
import { navbarRoute } from './layouts/navbar/navbar.route';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import { RechercheComponent } from 'app/entities/recherche/recherche.component';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { SearchComponent } from 'app/entities/search/search.component';
import { FileComponent } from 'app/entities/file/file.component';
import { TypeComponent } from 'app/entities/type/type.component';
import { DateComponent } from 'app/entities/date/date.component';
import { DescripComponent } from 'app/entities/descrip/descrip.component';
import { TitleComponent } from 'app/entities/title/title.component';
import { MotComponent } from 'app/entities/mot/mot.component';
import { AvanceComponent } from 'app/entities/avance/avance.component';
import { ResultatComponent } from 'app/entities/resultat/resultat.component';

const LAYOUT_ROUTES = [navbarRoute, ...errorRoute];

@NgModule({
  imports: [
    RouterModule.forRoot(
      [
        {
          path: 'recherche',
          component: RechercheComponent
        },
        {
          path: 'rslt',
          component: ResultatComponent
        },
        {
          path: 'avance',

          component: AvanceComponent
        },
        {
          path: 'folder/:id',

          component: SearchComponent
        },
        {
          path: 'file/:id',

          component: FileComponent
        },
        {
          path: 'type/:id',

          component: TypeComponent
        },
        {
          path: 'date/:id1/:id2',

          component: DateComponent
        },
        {
          path: 'desc/:id',

          component: DescripComponent
        },
        {
          path: 'title/:id',

          component: TitleComponent
        },
        {
          path: 'mot/:id',

          component: MotComponent
        },
        {
          path: 'admin',
          data: {
            authorities: ['ROLE_ADMIN']
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () => import('./admin/admin-routing.module').then(m => m.AdminRoutingModule)
        },

        {
          path: 'document',
          data: {
            authorities: ['ROLE_ADMIN', 'ROLE_USER']
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () => import('./document/document.module').then(m => m.DocumentModule)
        },
        {
          path: 'account',
          loadChildren: () => import('./account/account.module').then(m => m.AlfrescoGedAccountModule)
        },
        {
          path: 'rech/',
          loadChildren: () => import('./entities/entity.module').then(m => m.AlfrescoGedEntityModule)
        },

        ...LAYOUT_ROUTES
      ],
      { enableTracing: DEBUG_INFO_ENABLED }
    )
  ],
  exports: [RouterModule]
})
export class AlfrescoGedAppRoutingModule {}

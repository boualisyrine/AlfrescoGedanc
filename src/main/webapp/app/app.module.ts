import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { AlfrescoGedSharedModule } from 'app/shared/shared.module';
import { AlfrescoGedCoreModule } from 'app/core/core.module';
import { AlfrescoGedAppRoutingModule } from './app-routing.module';
import { AlfrescoGedHomeModule } from './home/home.module';
import { AlfrescoGedEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { JhiMainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { RechercheComponent } from 'app/entities/recherche/recherche.component';
import { SearchComponent } from 'app/entities/search/search.component';
import { RouterModule } from '@angular/router';
import { FileComponent } from 'app/entities/file/file.component';
import { TypeComponent } from 'app/entities/type/type.component';
import { ResultatComponent } from 'app/entities/resultat/resultat.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

@NgModule({
  imports: [
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    RouterModule,
    NgbModule,
    BrowserModule,
    AlfrescoGedSharedModule,
    AlfrescoGedCoreModule,
    AlfrescoGedHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    AlfrescoGedEntityModule,
    AlfrescoGedAppRoutingModule,
    FormsModule,
    BrowserAnimationsModule
  ],
  declarations: [
    JhiMainComponent,
    NavbarComponent,
    ErrorComponent,
    PageRibbonComponent,
    ActiveMenuDirective,
    FooterComponent,
    RechercheComponent,
    SearchComponent,
    FileComponent,
    TypeComponent,
    ResultatComponent
  ],
  bootstrap: [JhiMainComponent]
})
export class AlfrescoGedAppModule {}

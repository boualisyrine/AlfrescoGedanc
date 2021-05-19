import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
// import { RechercheComponent } from './recherche/recherche.component';
import { FormsModule } from '@angular/forms';
// import { SearchComponent } from './search/search.component';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { DateComponent } from './date/date.component';
import { DescripComponent } from './descrip/descrip.component';
import { TitleComponent } from './title/title.component';
import { MotComponent } from './mot/mot.component';
import { AvanceComponent } from './avance/avance.component';

@NgModule({
  imports: [
    CommonModule,
    NgbModule,
    FormsModule,
    RouterModule.forChild([
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [DateComponent, DescripComponent, TitleComponent, MotComponent, AvanceComponent],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AlfrescoGedEntityModule {}

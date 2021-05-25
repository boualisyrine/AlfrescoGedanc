import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DocumentRoutingModule } from './document-routing.module';
import { DocumentComponent } from './document.component';
import { TreeviewModule } from 'ngx-treeview';

@NgModule({
  declarations: [DocumentComponent],
  imports: [CommonModule, DocumentRoutingModule, TreeviewModule.forRoot()]
})
export class DocumentModule {}

import { Component, OnInit } from '@angular/core';
import { TreeviewItem } from 'ngx-treeview';
import { AlfrescoService } from 'app/core/alfresco/alfresco.service';

@Component({
  selector: 'jhi-document',
  templateUrl: './document.component.html',
  styleUrls: ['./document.component.scss']
})
export class DocumentComponent implements OnInit {
  config = {
    hasAllCheckBox: false,
    hasFilter: true,
    hasCollapseExpand: false,

    decoupleChildFromParent: false,
    maxHeight: 500
  };
  items: TreeviewItem[] = [];
  constructor(private alfrescoService: AlfrescoService) {}

  ngOnInit() {
    this.alfrescoService.fetchTree().subscribe(
      data => {
        this.items.push(new TreeviewItem(data));
      },
      ex => console.log(ex)
    );

    const item = new TreeviewItem(
      {
        text: 'IT',
        value: 9,

        children: [
          {
            text: 'Programming',
            value: 91,

            children: [
              {
                text: 'Frontend',
                value: 911,

                children: [
                  { text: 'Angular 1', value: 9111 },
                  { text: 'Angular 2', value: 9112 },
                  { text: 'ReactJS', value: 9113 }
                ]
              },
              {
                text: 'Backend',
                value: 912,
                children: [
                  { text: 'C#', value: 9121 },
                  { text: 'Java', value: 9122 },
                  { text: 'Python', value: 9123 }
                ]
              }
            ]
          },
          {
            text: 'Networking',
            value: 92,
            children: [
              { text: 'Internet', value: 921 },
              { text: 'Security', value: 922 }
            ]
          }
        ]
      },
      false
    );
    // this.items.push(item);
  }

  onSelectedChange(event) {
    console.log(event);
  }
}

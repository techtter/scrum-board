import { Component, OnInit } from '@angular/core';
import { Scrum } from '../model/scrum/scrum';
import { ScrumService } from '../service/scrum-service.service';
import { MatDialog, MatDialogConfig } from '@angular/material';
import { ScrumDialogComponent } from '../scrum-dialog/scrum-dialog.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  scrumList: Scrum[];

  constructor(
    private scrumService: ScrumService,
    private dialog: MatDialog
  ) { }

  ngOnInit() {
    this.retrieveAllScrumBoards();
  }

  openDialogForNewScrum(): void {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.autoFocus = true;
    dialogConfig.data = {
      scrum: new Scrum()
    };
    this.dialog.open(ScrumDialogComponent, dialogConfig)
  }

  private retrieveAllScrumBoards(): void {
    this.scrumService.retrieveAllScrumBoards().subscribe(

      response => {
        this.scrumList = response;
      }
    )
  }

}

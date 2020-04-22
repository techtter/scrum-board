import { Component, OnInit } from '@angular/core';
import { ScrumService } from '../service/scrum-service.service';
import { ActivatedRoute } from '@angular/router';
import { Scrum } from '../model/scrum/scrum';
import { Task } from '../model/task/task';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { MatDialog, MatDialogConfig } from '@angular/material';
import { TaskDialogComponent } from '../task-dialog/task-dialog.component';
import { TaskService } from '../service/task.service';

@Component({
  selector: 'app-scrum',
  templateUrl: './scrum.component.html',
  styleUrls: ['./scrum.component.css']
})
export class ScrumComponent implements OnInit {

  scrum: Scrum;
  todos: Task[] = [];
  inprogress: Task[] = [];
  dones: Task[] = [];

  constructor(
    private scrumService: ScrumService,
    private taskService: TaskService,
    private route: ActivatedRoute,
    private dialog: MatDialog
  ) { }

  ngOnInit() {
    this.getScrum();
  }

  drop(event: CdkDragDrop<string[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      this.updateTaskStatusAfterDragDrop(event);
      transferArrayItem(event.previousContainer.data,
                        event.container.data,
                        event.previousIndex,
                        event.currentIndex);
    }
  }

  openDialogForNewTask(): void {
    this.openDialog('Create New Task', new Task());
  }

  openTaskDialog(event): void {
    let taskId = event.srcElement.id;

    this.taskService.getTaskById(taskId).subscribe(
      response => {
        this.openDialog('Update Task', response);
      }
    );
  }

  private getScrum(): void {
    const id = this.route.snapshot.paramMap.get('id');

    this.scrumService.retrieveScrumById(id).subscribe(
      response => {
        this.scrum = response;
        this.splitTasksByStatus(response);
      }
    )
  }

  private splitTasksByStatus(scrum: Scrum): void {
    this.todos = scrum.tasks.filter(t=>t.status==='TODO');
    this.inprogress = scrum.tasks.filter(t=>t.status==='INPROGRESS');
    this.dones = scrum.tasks.filter(t=>t.status==='DONE');
  }

  private updateTaskStatusAfterDragDrop(event: CdkDragDrop<string[], string[]>) {
    let taskId = event.item.element.nativeElement.id;
    let containerId = event.container.id;

    this.taskService.getTaskById(taskId).subscribe(
        response => {
          this.updateTaskStatus(response, containerId);
        }
    );
  }

  private updateTaskStatus(task: Task, containerId: string): void {
    if (containerId === 'todo'){
      task.status = 'TODO'
    } else if (containerId === 'inpro'){
      task.status = 'INPROGRESS'
    } else {
      task.status = 'DONE'
    }
    this.taskService.updateTask(task).subscribe();
  }

  private openDialog(title: string, task: Task): void {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.autoFocus = true;
    dialogConfig.data = {
      title: title,
      task: task,
      scrumId: this.scrum.id
    };
    this.dialog.open(TaskDialogComponent, dialogConfig)
  }
}

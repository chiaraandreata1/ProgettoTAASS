import { Component, OnInit } from '@angular/core';
import {Observable} from "rxjs";
import {Board} from "../board";
import {BoardService} from "../board.service";

@Component({
  selector: 'app-boards-list',
  templateUrl: './boards-list.component.html',
  styleUrls: ['./boards-list.component.css']
})
export class BoardsListComponent implements OnInit {

  boards!: Observable<Board[]>;

  constructor(private boardService: BoardService) { }

  ngOnInit(): void {
    this.reloadData();
  }

  deleteBoards() {
    this.boardService.deleteAllBoards()
      .subscribe(
        data => {
          console.log(data);
          this.reloadData();
        },
        error => console.log('ERROR: ' + error));
  }

  reloadData() {
    this.boards = this.boardService.getBoardsList();
  }
}

import {Component, Input, OnInit} from '@angular/core';
import {Board} from "../../models/board";
import {BoardService} from "../../services/board.service";
import {BoardsListComponent} from "../boards-list/boards-list.component";

@Component({
  selector: 'board-details',
  templateUrl: './board-details.component.html',
  styleUrls: ['./board-details.component.css']
})
export class BoardDetailsComponent implements OnInit {

  @Input() board!: Board;

  constructor(private boardService: BoardService, private listComponent: BoardsListComponent) { }

  ngOnInit(): void {
  }

  deleteBoard() {
    this.boardService.deleteBoard(this.board.id)
      .subscribe(
        data => {
          console.log(data);
          this.listComponent.reloadData();
        },
        error => console.log(error));
  }
}

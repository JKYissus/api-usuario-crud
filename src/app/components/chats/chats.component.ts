import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ApiService } from 'src/app/services/api.service';

interface User {
  username: string;
  nombres: string;
  apellidos: string;
  correo: string;
  id: string;
}

@Component({
  selector: 'app-chats',
  templateUrl: './chats.component.html',
  styleUrls: ['./chats.component.scss']
})
export class ChatsComponent implements OnInit {

  mostrarMenu: boolean = false;
  userto: string = "";
  selectedUser: any = null;
  userselect: string = "";
  users: User[] = [];
  filteredUsers: User[] = [];

  constructor(
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.route.firstChild?.params.subscribe({
      next: (data: any) => {
        this.userto = data.id;
      }, error: (error) => {
      }
    })
  }

  autoResizeTextArea(textarea: any) {
    textarea.style.height = 'auto';
    textarea.style.height = Math.min(textarea.scrollHeight, 65) + 'px';
  }
}


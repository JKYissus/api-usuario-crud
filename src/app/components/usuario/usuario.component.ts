import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Route, Router, RouterLinkActive } from '@angular/router';
import { ApiService } from 'src/app/services/api.service';
import { SwAlertService } from 'src/app/services/sw.alert.service';

interface User {
  username: string;
  nombres: string;
  apellidos: string;
  correo: string;
}

interface Paginado {
  totalPaginas: number;
  numPagina: number;
  tamPagina: number;
}

interface UserSession {
  id: string;
  correo: string;
  rol: string;
  username: string;
}

@Component({
  selector: 'app-usuario',
  templateUrl: './usuario.component.html',
  styleUrls: ['./usuario.component.scss']
})
export class UsuarioComponent implements OnInit {

  searchText: string = '';
  filteredUsers: User[] = [];
  currentPage: number = 0;
  pageSize: number = 8;
  actualUser: number = 0;
  buscarValido: boolean = false;
  totalPaginas: number = 0;

  users: User[] = [];
  paginado: Paginado = {
    totalPaginas: 0,
    numPagina: 0,
    tamPagina: 0
  };

  searchForm = new FormGroup({ usuario: new FormControl('', [Validators.required]) })

  constructor(
    private service: ApiService,
    private Swal: SwAlertService,
    private router: Router
  ) { }

  get totalPages(): number {
    return Math.ceil(this.filteredUsers.length / this.pageSize);
  }

  get pages(): number[] {
    return Array.from({ length: this.totalPages }, (_, index) => index + 2);
  }

  ngOnInit() {
    this.getUsers();
    this.filteredUsers = this.users;
  }

  goToPage(page: number) {
    this.currentPage = page - 1;
    this.getUsers();
  }

  prevPage() {
    if (this.currentPage < 1) {
      return;
    }
    this.currentPage--;
    this.getUsers();

  }

  nextPage() {
    if (this.currentPage === this.totalPages + 1) {
      return;
    }
    this.currentPage++;
    this.getUsers();
  }

  get mostrarTotalPaginas(): number[] {

    return Array(this.totalPaginas).fill(1).map((_, index) => index + 1);

  }

  searchFromSubmit() {
    this.currentPage = 0;
    this.getUsers();
  }


  getUsers() {

    this.service.getUsers(this.currentPage, this.pageSize, this.searchForm.value.usuario!).subscribe({
      next: (data: any) => {

        if (data.data.paginado.totalPaginas >= 1) {

          this.totalPaginas = data.data.paginado.totalPaginas;

          this.filteredUsers = data.data.usuarios.map((user: User) => {
            return {
              username: user.username,
              nombres: user.nombres,
              apellidos: user.apellidos,
              correo: user.correo
            };
          });
          let userid: UserSession = JSON.parse(sessionStorage.getItem('user')!.toString());
          this.filteredUsers = this.filteredUsers.filter(function (el) { return el.username != userid.username; });

        } else {
          this.Swal.showError("El usuario no se encontro").then((result) => {
            if (result.isConfirmed) {
              return;
            }
          });
        }
      }
      ,
      error: (error: any) => {
        console.log(error.error);
      }
    });
  }

  chatUsuario(user: string) {
    this.router.navigate(['/dashboard/chats/' + user]);
  }
}

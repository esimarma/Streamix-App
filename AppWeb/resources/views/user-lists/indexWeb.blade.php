<x-app-layout>
    <x-slot name="header">
        <h2 class="font-semibold text-xl text-gray-800 leading-tight">
            {{ __('Utilizadores - Listas') }}
        </h2>
    </x-slot>

    <div class="py-12">
        <div class="max-w-7xl mx-auto sm:px-6 lg:px-8">
            <div class="bg-white overflow-hidden shadow-sm sm:rounded-lg">
                <div class="p-6">
                    <label for="search" class="mb-4">Pesquisar:</label>
                    <form method="GET" action="{{ route('user-lists.indexWeb') }}" style="display: flex; gap: 10px; align-items: center;" class="mb-4 flex items-center space-x-2">
                        <input 
                            type="text" 
                            name="user_name" 
                            id="user_name"
                            placeholder="Nome do Utilizador"
                            value="{{ request('user_name') }}" 
                            class="border rounded px-4 py-2 "
                        />
                        <input 
                            type="text" 
                            name="list_name" 
                            id="list_name"
                            placeholder="Nome da Lista" 
                            value="{{ request('list_name') }}" 
                            class="border rounded px-4 py-2"
                        />
                        <button 
                            type="submit" 
                            class="bg-blue-500 px-4 py-2 rounded hover:bg-blue-600 flex items-center justify-center">
                            <!-- Ícone de lupa -->
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                                <path stroke-linecap="round" stroke-linejoin="round" d="M21 21l-4.35-4.35m1.41-5.18a7 7 0 11-14 0 7 7 0 0114 0z" />
                            </svg>
                        </button>
                    </form>
                    <table class="min-w-full border-collapse border border-gray-200">
                        <thead>
                            <tr class="bg-gray-100">
                                <th class="border border-gray-300 px-4 py-2 text-left">ID</th>
                                <th class="border border-gray-300 px-4 py-2 text-left">Utilizador</th>
                                <th class="border border-gray-300 px-4 py-2 text-left">Nome da Lista</th>
                                <th class="border border-gray-300 px-4 py-2 text-left">Tipo de Lista</th>
                                <th class="border border-gray-300 px-4 py-2 text-left">Ver Média</th>
                                <th class="border border-gray-300 px-4 py-2 text-left">Ações</th>
                            </tr>
                        </thead>
                        <tbody>
                            @foreach ($userList as $list)
                            <tr>
                                <td class="border border-gray-300 px-4 py-2">{{ $list->id }}</td>
                                <td class="border border-gray-300 px-4 py-2">{{ $list->user->name }}</td>
                                <td class="border border-gray-300 px-4 py-2">
                                    <input readonly type="text" name="nome" value="{{ $list->name }}" style="border: none; outline: none; background: transparent;">
                                </td>
                                <td class="border border-gray-300 px-4 py-2">
                                    <input readonly type="text" name="tipo" value="{{ $list->list_type }}" style="border: none; outline: none; background: transparent;">
                                </td>
                                <td class="border border-gray-300 px-4 py-2">
                                    <button 
                                        class="text-blue-500 hover:underline" >
                                        Ver Média
                                    </button>
                                </td>
                                <td class="border border-gray-300 px-4 py-2">
                                    <button class="text-blue-500 hover:underline editar">
                                    <svg width="20px" height="20px" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg" fill="none">
                                        <path fill="#000000" fill-rule="evenodd" d="M15.198 3.52a1.612 1.612 0 012.223 2.336L6.346 16.421l-2.854.375 1.17-3.272L15.197 3.521zm3.725-1.322a3.612 3.612 0 00-5.102-.128L3.11 12.238a1 1 0 00-.253.388l-1.8 5.037a1 1 0 001.072 1.328l4.8-.63a1 1 0 00.56-.267L18.8 7.304a3.612 3.612 0 00.122-5.106zM12 17a1 1 0 100 2h6a1 1 0 100-2h-6z"/>
                                    </svg>
                                    </button>
                                    <button class="text-red-500 hover:underline ml-4 excluir">
                                    <svg width="22px" height="22px" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                                        <path d="M10 12V17" stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                                        <path d="M14 12V17" stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                                        <path d="M4 7H20" stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                                        <path d="M6 10V18C6 19.6569 7.34315 21 9 21H15C16.6569 21 18 19.6569 18 18V10" stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                                        <path d="M9 5C9 3.89543 9.89543 3 11 3H13C14.1046 3 15 3.89543 15 5V7H9V5Z" stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                                    </svg>
                                    </button>
                                </td>
                            </tr>
                            @endforeach
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <!-- JavaScript -->
    <script>
        document.querySelectorAll('.editar').forEach(button => {
            button.addEventListener('click', function () {
                const row = this.closest('tr');
                const isEditing = !row.querySelector('input').readOnly;

                if (isEditing) {
                    // Salvar as alterações via AJAX
                    const id = row.querySelector('td:first-child').textContent.trim();
                    const nome = row.querySelector('input[name="nome"]').value;
                    const tipo = row.querySelector('input[name="tipo"]').value;

                    fetch(`/user-lists/${id}`, {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json',
                            'X-CSRF-TOKEN': document.querySelector('meta[name="csrf-token"]').getAttribute('content')
                        },
                        body: JSON.stringify({ nome, tipo })
                    })
                    .then(response => {
                        if (response.ok) {
                            alert('Lista atualizada com sucesso!');
                        } else {
                            alert('Erro ao atualizar a lista.');
                        }
                    })
                    .catch(error => {
                        console.error('Erro:', error);
                        alert('Erro ao atualizar a lista.');
                    });

                    // Voltar ao modo readonly
                    row.querySelectorAll('input').forEach(input => {
                        input.readOnly = true;
                        input.style.border = "none";
                        input.style.background = "transparent";
                    });
                    this.innerHTML = `
                        <svg width="20px" height="20px" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg" fill="none">
                            <path fill="#000000" fill-rule="evenodd" d="M15.198 3.52a1.612 1.612 0 012.223 2.336L6.346 16.421l-2.854.375 1.17-3.272L15.197 3.521zm3.725-1.322a3.612 3.612 0 00-5.102-.128L3.11 12.238a1 1 0 00-.253.388l-1.8 5.037a1 1 0 001.072 1.328l4.8-.63a1 1 0 00.56-.267L18.8 7.304a3.612 3.612 0 00.122-5.106zM12 17a1 1 0 100 2h6a1 1 0 100-2h-6z"/>
                        </svg>`;
                } else {
                    // Tornar os inputs editáveis
                    row.querySelectorAll('input').forEach(input => {
                        input.readOnly = false;
                        input.style.border = "1px solid #ccc";
                        input.style.background = "#fff";
                    });
                    // Alterar ícone para "Salvar"
                    this.innerHTML = `
                        <svg width="20px" height="20px" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path fill-rule="evenodd" clip-rule="evenodd" d="M18.1716 1C18.702 1 19.2107 1.21071 19.5858 1.58579L22.4142 4.41421C22.7893 4.78929 23 5.29799 23 5.82843V20C23 21.6569 21.6569 23 20 23H4C2.34315 23 1 21.6569 1 20V4C1 2.34315 2.34315 1 4 1H18.1716ZM4 3C3.44772 3 3 3.44772 3 4V20C3 20.5523 3.44772 21 4 21L5 21L5 15C5 13.3431 6.34315 12 8 12L16 12C17.6569 12 19 13.3431 19 15V21H20C20.5523 21 21 20.5523 21 20V6.82843C21 6.29799 20.7893 5.78929 20.4142 5.41421L18.5858 3.58579C18.2107 3.21071 17.702 3 17.1716 3H17V5C17 6.65685 15.6569 8 14 8H10C8.34315 8 7 6.65685 7 5V3H4ZM17 21V15C17 14.4477 16.5523 14 16 14L8 14C7.44772 14 7 14.4477 7 15L7 21L17 21ZM9 3H15V5C15 5.55228 14.5523 6 14 6H10C9.44772 6 9 5.55228 9 5V3Z" fill="#0F0F0F"/>
                        </svg>`;
                }
            });
        });
        document.querySelectorAll('.excluir').forEach(button => {
            button.addEventListener('click', function () {
                const row = this.closest('tr');
                const id = row.querySelector('td:first-child').textContent.trim();

                // Exibe uma confirmação antes de excluir
                const confirmar = confirm('Tem certeza que deseja excluir esta lista?');
                if (confirmar) {
                    // Envia a solicitação para excluir
                    fetch(`/user-lists/${id}`, {
                        method: 'DELETE',
                        headers: {
                            'Content-Type': 'application/json',
                            'X-CSRF-TOKEN': document.querySelector('meta[name="csrf-token"]').getAttribute('content')
                        },
                    })
                    .then(response => {
                        if (response.ok) {
                            // Remove a linha da tabela
                            row.remove();
                            alert('Lista excluída com sucesso!');
                        } else {
                            alert('Erro ao excluir a lista.');
                        }
                    })
                    .catch(error => {
                        console.error('Erro:', error);
                        alert('Erro ao excluir a lista.');
                    });
                }
            });
        });
    </script>
</x-app-layout>
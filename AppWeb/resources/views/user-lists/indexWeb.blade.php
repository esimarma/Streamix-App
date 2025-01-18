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
                                    <button class="text-blue-500 hover:underline editar">Editar</button>
                                    <button class="text-red-500 hover:underline ml-2 excluir">Excluir</button>
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
                    this.textContent = "Editar";
                } else {
                    // Tornar os inputs editáveis
                    row.querySelectorAll('input').forEach(input => {
                        input.readOnly = false;
                        input.style.border = "1px solid #ccc";
                        input.style.background = "#fff";
                    });
                    this.textContent = "Salvar";
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
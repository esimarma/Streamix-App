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
                                    <button class="text-red-500 hover:underline ml-2">Excluir</button>
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
        // Seleciona todos os botões de edição
        document.querySelectorAll('.editar').forEach(button => {
            button.addEventListener('click', function () {
                // Encontra a linha mais próxima do botão
                const row = this.closest('tr');
                // Alterna o estado de readonly nos inputs dentro dessa linha
                row.querySelectorAll('input').forEach(input => {
                    input.readOnly = !input.readOnly;
                    if (!input.readOnly) {
                        input.style.border = "1px solid #ccc"; // Adiciona borda ao ficar editável
                        input.style.background = "#fff"; // Adiciona fundo branco
                    } else {
                        input.style.border = "none"; // Remove borda ao voltar a readonly
                        input.style.background = "transparent"; // Remove fundo
                    }
                });
                // Muda o texto do botão para "Salvar" enquanto está editável
                this.textContent = row.querySelector('input').readOnly ? "Editar" : "Salvar";
            });
        });
    </script>
</x-app-layout>
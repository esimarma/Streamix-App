<x-app-layout>
    <x-slot name="header">
        <h2 class="font-semibold text-xl text-gray-800 leading-tight">
            {{ __('Utilizadores - Avaliações') }}
        </h2>
    </x-slot>

    <div class="py-12">
        <div class="max-w-7xl mx-auto sm:px-6 lg:px-8">
            <div class="bg-white overflow-hidden shadow-sm sm:rounded-lg">
                <div class="p-6">
                    <label for="search" class="mb-4">Pesquisar:</label>
                    <form method="GET" action="{{ route('ratings.indexWeb', ['userId' => $userId]) }}" style="display: flex; gap: 10px; align-items: center;" class="mb-4 flex items-center space-x-2">
                        <input 
                            type="text" 
                            name="user_name" 
                            id="user_name"
                            placeholder="Nome do Utilizador"
                            value="{{ request('user_name') }}" 
                            class="border rounded px-4 py-2 "
                        />
                        <select name="type" id="type" class="border rounded w-64" onchange="this.form.submit()">
                            <option value="">Todos</option>
                            <option value="tv" {{ request('type') == 'tv' ? 'selected' : '' }}>TV</option>
                            <option value="movie" {{ request('type') == 'movie' ? 'selected' : '' }}>Filme</option>
                        </select>
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
                                <th class="border border-gray-300 px-4 py-2 text-left">Utilizador</th>
                                <th class="border border-gray-300 px-4 py-2 text-left">Media</th>
                                <th class="border border-gray-300 px-4 py-2 text-left">Tipo de Media</th>
                                <th class="border border-gray-300 px-4 py-2 text-left">Avaliação</th>
                                <th class="border border-gray-300 px-4 py-2 text-left">Ações</th>
                            </tr>
                        </thead>
                        <tbody>
                            @foreach ($ratingsList as $list)
                            <tr data-id="{{ $list->id }}">
                                <td class="border border-gray-300 px-4 py-2">{{ $list->user->name }}</td>
                                <td class="border border-gray-300 px-4 py-2">{{ $list->media }}</td>
                                <td class="border border-gray-300 px-4 py-2">{{ $list->mediaType }}</td>
                                <td class="border border-gray-300 px-4 py-2">{{ $list->rating }}</td>
                                <td class="border border-gray-300 px-4 py-2">
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
        document.querySelectorAll('.excluir').forEach(button => {
            button.addEventListener('click', function () {
                const row = this.closest('tr');
                const id = row.getAttribute('data-id');

                // Exibe uma confirmação antes de excluir
                const confirmar = confirm('Tem certeza que deseja excluir esta lista?');
                if (confirmar) {
                    // Envia a solicitação para excluir
                    fetch(`/ratings/${id}`, {
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
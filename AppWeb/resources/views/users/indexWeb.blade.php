<x-app-layout>
    <x-slot name="header">
        <h2 class="font-semibold text-xl text-gray-800 leading-tight">
            {{ __('Utilizadores') }}
        </h2>
    </x-slot>

    <div class="py-12">
        <div class="max-w-7xl mx-auto sm:px-6 lg:px-8">
            <div class="bg-white overflow-hidden shadow-sm sm:rounded-lg">
                <div class="p-6">
                    <label for="search" class="mb-4">Pesquisar:</label>
                    <form method="GET" action="{{ route('users.indexWeb') }}" class="mb-4 flex items-center space-x-2">
                        <input 
                            type="text" 
                            name="search" 
                            placeholder="Nome do Utilizador" 
                            value="{{ request('search') }}" 
                            class="border rounded px-4 py-2"
                        />
                        <button 
                            type="submit" 
                            class="bg-blue-500 px-4 py-2 rounded hover:bg-blue-600 flex items-center justify-center" >
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
                                <th class="border border-gray-300 px-4 py-2 text-left">Nome</th>
                                <th class="border border-gray-300 px-4 py-2 text-left">Email</th>
                                <th class="border border-gray-300 px-4 py-2 text-left">Tempo Series</th>
                                <th class="border border-gray-300 px-4 py-2 text-left">Tempo Filmes</th>
                                <th class="border border-gray-300 px-4 py-2 text-left">Listas</th>
                                <th class="border border-gray-300 px-4 py-2 text-left">Avaliações</th>
                            </tr>
                        </thead>
                        <tbody>
                            @foreach ($users as $user)
                            <tr>
                                <td class="border border-gray-300 px-4 py-2">{{ $user->id }}</td>
                                <td class="border border-gray-300 px-4 py-2">{{ $user->name }}</td>
                                <td class="border border-gray-300 px-4 py-2">{{ $user->email }}</td>
                                <td class="border border-gray-300 px-4 py-2">{{ $user->series_wasted_time_min }}</td>
                                <td class="border border-gray-300 px-4 py-2">{{ $user->	movie_wasted_time_min }}</td>
                                <td class="border border-gray-300 px-4 py-2">
                                <button 
                                    class="text-blue-500 hover:underline ver-listas-btn" 
                                    data-id="{{ $user->id }}">
                                    <a href="{{ route('user-lists.indexWeb', ['userId' => $user->id]) }}">Ver Listas</a>
                                </button>
                                </td>
                                <td class="border border-gray-300 px-4 py-2">
                                <button 
                                    class="text-blue-500 hover:underline ver-listas-btn" 
                                    data-id="{{ $user->id }}">
                                    <a href="{{ route('ratings.indexWeb', ['userId' => $user->id]) }}">Ver Avaliações</a>
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
</x-app-layout>

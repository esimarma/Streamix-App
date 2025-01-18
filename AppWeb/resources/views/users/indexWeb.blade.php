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
                <form method="GET" action="{{ route('users.indexWeb') }}" class="mb-4 flex items-center space-x-2">
                    <input 
                        type="text" 
                        name="search" 
                        placeholder="Pesquisar utilizadores..." 
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
                                <th class="border border-gray-300 px-4 py-2 text-left">Ações</th>
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
                                    <a href="{{ route('user-lists.lists', ['id' => $user->id]) }}">Ver Listas</a>
                                </button>
                            </td>
                                <td class="border border-gray-300 px-4 py-2 text-center">
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
</x-app-layout>

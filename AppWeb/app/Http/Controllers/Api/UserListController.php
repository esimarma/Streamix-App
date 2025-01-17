<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Http\Requests\StoreUserListRequest;
use App\Http\Requests\SearchRequest;
use Illuminate\Http\Request;
use App\Http\Resources\UserListResource;
use App\Models\UserList;

class UserListController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index(SearchRequest $request)
    {
        $query = UserList::query();
        
        // Verifica se há um termo de pesquisa
        if ($request->has('search') && $request->search) {
            $search = $request->search;
            $query->where('name', 'LIKE', "%{$search}%");
        }
        
        // Eager Loading da relação 'user'
        $userList = $query->with('user')->paginate(10);
    
        return view('user-lists.index', compact('userList')); // Passando $userList para a view
    }

    public function getByUser($userId)
    {
        // Busca todas as listas associadas ao utilizador pelo ID
        $userLists = UserList::where('id_user', $userId)->get();
    
        // Retorna as listas como uma coleção de recursos
        return UserListResource::collection($userLists);
    }

    /**
     * Show the form for creating a new resource.
     */
    public function create()
    {
        //
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(StoreUserListRequest $request)
    {
        $userList = UserList::create($request->validated());
        return new UserListResource($userList);
    }

    /**
     * Display the specified resource.
     */
    public function show(string $id)
    {
        $userList = UserList::findOrFail($id);
        return new UserListResource($userList);
    }

    /**
     * Show the form for editing the specified resource.
     */
    public function edit(UserList $userList)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(StoreUserListRequest $request, UserList $userList)
    {
        $userList->update($request->validated());
        return new UserListResource($userList);
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(UserList $userList)
    {
        $userList->delete();
        return new UserListResource($userList);
    }
}

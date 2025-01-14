<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Http\Resources\UserListResource;
use App\Models\UserList;

class UserListController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $userList = UserList::all();
        return UserListResource::collection($userList);
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
    public function store(Request $request)
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
    public function update(Request $request, UserList $userList)
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

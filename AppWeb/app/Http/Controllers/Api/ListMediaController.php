<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\ListMedia;
use App\Http\Resources\ListMediaResource;
use App\Http\Requests\StoreListMediaRequest;

class ListMediaController extends Controller
{
    /*
* Display a listing of the resource.
*/
public function index()
{
   $listMedias = ListMedia::all();
   return ListMediaResource::collection($listMedias);
}

/*
* Show the form for creating a new resource.
*/
public function create()
{
   //
}

/*
* Store a newly created resource in storage.
*/
public function store(StoreListMediaRequest $request)
{
   $listMedias = ListMedia::create($request->validated());
   return new ListMediaResource($listMedias);
}

/*
* Display the specified resource.
*/
public function show(string $id)
{
   $listMedias = ListMedia::findOrFail($id);
   return new ListMediaResource($listMedias);
}

/*
* Show the form for editing the specified resource.
*/
public function edit(ListMedia $listMedias)
{
    //
}

/*
* Update the specified resource in storage.
*/
public function update(StoreListMediaRequest $request, ListMedia $listMedias)
{
   $listMedias->update($request->validated());
   return new ListMediaResource($listMedias);
}

/*
* Remove the specified resource from storage.
*/
public function destroy(ListMedia $listMedias)
{
   $listMedias->delete();
   //return response(null, 204);
   // return new ListMediaResource(null);
   return new ListMediaResource($listMedias);
}
}

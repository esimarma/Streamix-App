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
      $validated = $request->validate([
         'id_list_user' => 'required|integer',
         'id_media' => 'required|string',
   ]);

   $listMedia = ListMedia::create($validated);

   return response()->json([
         'message' => 'Mídia criada com sucesso!',
         'data' => $listMedia,
   ], 201);
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
   public function update(StoreListMediaRequest $request, ListMedia $listMedia)
   {
      $listMedia->update($request->validated());

      return new ListMediaResource($listMedia);
   }

   /*
   * Remove the specified resource from storage.
   */
   public function destroy(ListMedia $listMedia)
   {
         $listMedia->delete();
         return response()->json([
            'message' => 'Registro excluído com sucesso!',
            'data' => null,
         ], 200);
   }
}

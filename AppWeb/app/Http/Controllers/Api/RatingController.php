<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Rating;
use App\Http\Resources\RatingResource;
use App\Http\Requests\StoreRatingRequest;

class RatingController extends Controller
{
    public function index()
    {
       $ratings = Rating::all();
       return RatingResource::collection($ratings);
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
    public function store(StoreRatingRequest $request)
    {
       $rating = Rating::create($request->validated());
       return new RatingResource($rating);
    }
    
    /*
    * Display the specified resource.
    */
    public function show(string $id)
    {
       $rating = Rating::findOrFail($id);
       return new RatingResource($rating);
    }
    
    /*
    * Show the form for editing the specified resource.
    */
    public function edit(Rating $Rating)
    {
        //
    }
    
    /*
    * Update the specified resource in storage.
    */
    public function update(StoreRatingRequest $request, Rating $rating)
    {
       $rating->update($request->validated());
       return new RatingResource($rating);
    }
    
    /*
    * Remove the specified resource from storage.
    */
    public function destroy(Rating $rating)
    {
       $rating->delete();
       //return response(null, 204);
       // return new RatingResource(null);
       return new RatingResource($rating);
    }
    
}

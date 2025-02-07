<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;

class Rating extends Model
{
    use HasFactory;

    protected $fillable = ['id_media', 'id_user', 'rating'];

    // Relação com o User
    public function user()
    {
        return $this->belongsTo(User::class, 'id_user');
    }
}

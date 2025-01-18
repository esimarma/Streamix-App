<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::table('list_media', function (Blueprint $table) {
            // Adicionar a chave estrangeira
            $table->foreign('id_list_user')->references('id')->on('user_lists')->onDelete('cascade');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('list_media', function (Blueprint $table) {
            // Remover a chave estrangeira
            $table->dropForeign(['id_list_user']);
        });
    }
};

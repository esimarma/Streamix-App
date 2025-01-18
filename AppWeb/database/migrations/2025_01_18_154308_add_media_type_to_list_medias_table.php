<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::table('list_media', function (Blueprint $table) {
            $table->string('media_type')->after('id_media')->default('movie'); // Adiciona a coluna 'media_type' com valor padrão 'movie'
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::table('list_media', function (Blueprint $table) {
            $table->dropColumn('media_type'); // Remove a coluna 'media_type' caso a migration seja revertida
        });
    }
};

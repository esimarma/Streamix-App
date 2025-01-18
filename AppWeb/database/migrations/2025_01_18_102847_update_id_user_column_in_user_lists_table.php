<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up()
    {
        Schema::table('user_lists', function (Blueprint $table) {
            $table->unsignedBigInteger('id_user')->change();
        });
    }

    public function down()
    {
        Schema::table('user_lists', function (Blueprint $table) {
            $table->string('id_user', 255)->change();
        });
    }
};

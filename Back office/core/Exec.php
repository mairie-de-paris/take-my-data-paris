<?php

/**
 * Copyright 2012-2013 The Apache Software Foundation
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * */
defined('EXEC') or die('Access forbidden');

final class Exec {

    private function __construct() {
        
    }

    /**
     * Dump a variable
     * 
     * @param mixed $obj The variable to dump
     */
    public static function d($obj) {
        if (DEBUG) {
            var_dump($obj);
        }
    }

    /**
     * Stop the execution of the current Page
     * 
     * @param string $why A message to explain why
     */
    public static function e($why = null) {
        if (DEBUG) {
            die($why);
        } else {
            exit(1);
        }
    }

}

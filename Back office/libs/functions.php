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
**/

defined('EXEC') or die('Access forbidden');

function __autoload($class_name) {
    if (file_exists(ROOT . 'classes/' . $class_name . '.php')) {
        require_once(ROOT . 'classes/' . $class_name . '.php');
    } else {
        require_once(ROOT . 'core/' . $class_name . '.php');
    }
}

function deparse_date($french_date) {
    $parts = explode('/', $french_date);
    return ($parts[2] . '-' . $parts[1] . '-' . $parts[0]);
}
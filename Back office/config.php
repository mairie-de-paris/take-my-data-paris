<?php

/**
 * Copyright 2012-2013 Mairie de Paris
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

define('DEBUG', 0);
define('ROOT', rtrim(dirname(__FILE__), '/') . '/');
define('BASE_ROOT', rtrim(dirname($_SERVER['SCRIPT_NAME']), '/'));
define('CONTENT_ROOT', BASE_ROOT . '/content/');
define('MODULES_ROOT', BASE_ROOT . '/modules/');

/**
 * Site URL
 */
define('SITE_URL', 'http://[SITE_URL]/');


define('TRUST_LIMIT', 60);
define('DEFAULT_LANGUAGE', 'fr');

if (!empty($_COOKIE['lang'])) {
    define('LANGUAGE', $_COOKIE['lang']);
} else {
    define('LANGUAGE', DEFAULT_LANGUAGE);
}

session_start();

    /**
     * Database server address
     */
$__host = 'localhost';

    /**
     * Database name
     */
$__dbname = '';

    /**
     * Database username
     */
$__username = '';

    /**
     * Database password
     */
$__password = '';

require_once(ROOT . 'libs/rights.php');
require_once(ROOT . 'libs/functions.php');
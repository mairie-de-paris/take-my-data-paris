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
 *limitations under the License.
**/

define('EXEC', 0xA51D1E);

require_once('../config.php');

if (!empty($_POST['type']) && !empty($_POST['source'])) {
    $parsor_name = Parsor::getParsor(intval($_POST['type']));
    $parsor = new $parsor_name($_POST['source']);

    if ($parsor->checkSource()) {
        die('ok');
    }
}
die('ko');
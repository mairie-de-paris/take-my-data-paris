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

$datasets = Dataset::getAll();
$liste_datasets = array();

foreach ($datasets as $dataset) {
    $liste_datasets[] = (object) array(
                'delete' => false,
                'url_picto' => stripslashes(SITE_URL . CONTENT_ROOT . 'picto' . $dataset->getID() . '.png'),
                'url_img' => stripslashes(SITE_URL . CONTENT_ROOT . $dataset->getID() . '.png'),
                'name' => $dataset->getName(true),
                'id' => $dataset->getID(),
                'description' => $dataset->getDesc(true)
    );
}

echo stripslashes(json_encode($liste_datasets));
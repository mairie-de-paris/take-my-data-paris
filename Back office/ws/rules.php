<?php

/**
 * Copyright 1999-2004 The Apache Software Foundation
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
$liste_rules = array();

foreach ($datasets as $dataset) {
    $parsor = Parsor::getFromID($dataset->getID());
    //{"delete":false, "name":"","id":1,"description":"","format":"","user_defined":true,"metadata":["les vieux","les lapins","les petits enfants"]}
    foreach ($parsor->getRules() as $rule) {
        $liste_rules[] = (object) array(
                    'delete' => false,
                    'name' => $rule->getName(),
                    'id' => $rule->getID(),
                    'description' => $rule->getDesc(),
                    'format' => $rule->getType(),
                    'user_defined' => true,
                    'metadata' => $rule->getData()
        );
    }
}

echo stripslashes(json_encode($liste_rules));
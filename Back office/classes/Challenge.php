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

class Challenge {

    private $id = 0;
    private $number = 0;
    private $dataset = 0;

    public function __construct(stdClass $row) {
        $this->id = intval($row->id);
        $this->dataset = doubleval($row->dataset);
        $this->number = doubleval($row->number);
    }

    public static function create(array $data) {
        DBO::query('INSERT INTO `points`
                (`name`, `description`, `dataset`, `goal`)
                VALUES
                ("", "",
                 ' . intval($data['dataset']) . ',
                 ' . intval($data['number']) . ');');
    }

}
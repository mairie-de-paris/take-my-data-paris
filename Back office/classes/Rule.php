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

class Rule {

    private $id = 0;
    private $name = null;
    private $type = null;
    private $desc = null;
    private $data = array();
    private $parsor = 0;

    public static function getQueryFields() {
        return (new QueryDescriptor('rules',
                        array('id' => 'RID',
                            'parsor' => 'Rparsor',
                            'name' => 'Rname',
                            'description' => 'Rdesc',
                            'type' => 'Rtype', // AHAH
                            'data' => 'Rdata')));
    }

    public function __construct(stdClass $row) {
        $this->id = intval($row->RID);
        $this->name = $row->Rname;
        $this->type = $row->Rtype;
        $this->desc = $row->Rdesc;
        $this->data = unserialize($row->Rdata);
        $this->parsor = intval($row->Rparsor);
    }

    public static function updateChoices($rule, $desc, array $choices) {
        $query = 'UPDATE `rules`
                  SET `data` = ' . DBO::escape(serialize($choices)) . ',
                      `description` = ' . DBO::escape($desc) . '
                  WHERE `id` = ' . intval($rule);
        DBO::query($query);
        return (true);
    }

    public function getName() {
        return (str_replace('"', "'", $this->name));
    }

    public function getType() {
        return ($this->type);
    }

    public function getID() {
        return ($this->id);
    }

    public function getData() {
        return ($this->data);
    }

    public function getDesc() {
        return (str_replace('"', "'", $this->desc));
    }

}
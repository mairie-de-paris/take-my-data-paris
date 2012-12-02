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

final class Dataset {

    private $id = 0;
    private $name = null;
    private $status = 0;
    private $home = 0;
    private $desc = null;

    private function __construct(stdClass $row) {
        $this->id = intval($row->id);
        $this->name = $row->name;
        $this->desc = $row->desc;
        $this->status = intval($row->status);
        $this->home = intval($row->home);
    }

    public static function getAll() {
        $liste = array();

        $res = DBO::query('SELECT `datasets`.* FROM `datasets` ORDER BY `id` DESC;');
        if ($res) {
            while (($row = $res->fetch_object())) {
                $liste[] = new Dataset($row);
            }
        }
        return ($liste);
    }

    public static function getFromID($id) {
        $res = DBO::query('SELECT `datasets`.* FROM `datasets` WHERE `id` = ' . intval($id));

        if (($row = $res->fetch_object())) {
            return (new Dataset($row));
        }
        return (null);
    }

    //Gtor/Ctor

    public function getID() {
        return ($this->id);
    }

    public function getName($utf8 = false) {
        if ($utf8) {
            return (utf8_encode($this->name));
        } else {
            return (htmlentities($this->name));
        }
    }

    public function getStatus() {
        return ($this->status);
    }

    public function getStatusStr() {
        if ($this->status === 1) {
            return ('D&eacute;publi&eacute;');
        } else if ($this->status === 0) {
            return ('Publi&eacute;');
        } else {
            die('Status ' . $this->status . ' not implemented in ' . __FILE__ . ' at line ' . (__LINE__ - 1));
        }
    }

    public static function create(array $source, array $picture, array $picto) {
        $query = 'INSERT INTO `datasets`
            (`name`, `desc`)
            VALUES
            (' . DBO::escape($source['name']) . ',
             ' . DBO::escape($source['desc']) . ');';

        if (($_POST['id'] = DBO::query($query)) !== 0) {
            Parsor::create($_POST);
            move_uploaded_file($picture['tmp_name'], ROOT . 'content' . DIRECTORY_SEPARATOR . $_POST['id'] . '.png');
            move_uploaded_file($picto['tmp_name'], ROOT . 'content' . DIRECTORY_SEPARATOR . 'picto' . $_POST['id'] . '.png');
        }
        return ($_POST['id']);
    }

    public function getHome() {
        return ($this->home);
    }

    public function getDesc($utf8 = false) {
        if ($utf8) {
            return (utf8_encode($this->desc));
        } else {
            return (htmlentities($this->desc));
        }
    }

}
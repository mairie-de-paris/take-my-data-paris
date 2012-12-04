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

class Point {

    private $id = 0;
    private $lat = 0.;
    private $lon = 0.;
    private $data = array();
    private $time;
    private $sourced = 0;
    private $count = 0;

    public function __construct(stdClass $row) {
        $this->id = intval($row->id);
        $this->sourced = intval($row->sourced);
        $this->time = intval($row->update);
        $this->lat = doubleval($row->lat);
        $this->lon = doubleval($row->lon);
        $this->data = unserialize($row->data);
        $this->count = intval($row->count);
    }

    public static function create($dataset, $lat, $lon, array $data) {
        $query = 'INSERT INTO `points`
                (`dataset`, `sourced`, `lat`, `lon`, `update`, `data`)
                VALUES
                (' . intval($dataset) . ',
                 1,
                 ' . floatval($lat) . ',
                 ' . floatval($lon) . ',
                 ' . intval(time()) . ',
                 ' . DBO::escape(serialize($data)) . ');';
        return (DBO::query($query));
    }

    public function plusOne(array $data) {
        DBO::query('UPDATE `points`
                SET `count` = `count` + 1,
                `data` = ' . DBO::escape(serialize($data)) . ',
                `update` = ' . time() . '
                WHERE `id` = ' . intval($this->id));
    }

    public static function getFromParsor($parsor, $lastmod = 0) {
        $list = array();

        $res = DBO::query('SELECT *
                    FROM `points`
                    WHERE `dataset` = ' . $parsor . '
                    AND `update` >= ' . intval($lastmod));

        while (($row = $res->fetch_object())) {
            $list[] = new Point($row);
        }
        return ($list);
    }

    public function getID() {
        return ($this->id);
    }

    public function getData() {
        return ($this->data);
    }

    public function getAData($data) {
        if (isset($this->data[$data])) {
            return ($this->data[$data]);
        } else {
            return (null);
        }
    }

    public static function getFromPos($dataset, $lat, $lon) {
        $query = 'SELECT * FROM `points` WHERE `dataset` = ' . intval($dataset);
        $query .= ' AND ABS(`lat` - ' . floatval($lat) . ') <= 0.00001';
        $query .= ' AND ABS(`lon` - ' . floatval($lon) . ') <= 0.00001';
        $res = DBO::query($query);
        if (($row = $res->fetch_object())) { //Already exist
            return (new Point($row));
        } else {
            return (null);
        }
    }

    public static function getFromID($id) {
        $query = 'SELECT * FROM `points` WHERE `id` = ' . intval($id);

        $res = DBO::query($query);
        if (($row = $res->fetch_object())) { //Already exist
            return (new Point($row));
        } else {
            return (null);
        }
    }

    public function getLat() {
        return ($this->lat);
    }

    public function getLon() {
        return ($this->lon);
    }

    public function getCount() {
        return ($this->count + $this->sourced);
    }

    public function getLastUpdate() {
        return ($this->time);
    }

}
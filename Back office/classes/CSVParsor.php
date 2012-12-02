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

class CSVParsor extends Parsor {

    private $data = null;

    public function __construct($source) {
        $this->source = $source;
    }

    public function checkSource($source = null) {
        if ($this->data === null) {
            if ($source !== null) {
                $this->data = $this->getURL($source);
            } else {
                $this->data = $this->getURL($this->source);
            }
            $this->data = preg_split('#((\r?\n)|(\r\n?))#', $this->data);
        }
        if (preg_match('#^.*?;.*?(;.*?)*$#ism', $this->data[0])) {
            return (true);
        } else {
            return (false);
        }
    }

    public function getColumns() {
        if ($this->data === null) {
            $this->data = $this->getURL($this->source);
            $this->data = preg_split('#((\r?\n)|(\r\n?))#', $this->data);
        }
        $parts = explode(';', $this->data[0]);
        $count = count($parts);
        $i = 0;
        $cols = array();

        while ($i < $count) {
            $cols[] = '' . ($i + 1);
            ++$i;
        }

        return ($cols);
    }

    public function getCol($row, $col) {
        $parts = explode(';', $row);
        $col = intval($col) - 1;
        if ($col >= 0 && isset($parts[$col])) {
            return ($parts[$col]);
        } else {
            return (null);
        }
    }

    public function getRows() {
        if ($this->data === null) {
            $this->data = $this->getURL($this->source);
            $this->data = preg_split('#((\r?\n)|(\r\n?))#', $this->data);
        }
        return ($this->data);
    }

}

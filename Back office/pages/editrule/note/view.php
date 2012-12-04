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

?>
<form class="center" action="<?= Url::toEditParsorRule($this->parsor, $this->rule); ?>" method="post">
    <div id="accordion">
        <h3><a href="#">Description</a></h3>
        <div>
            Une description courte (32 caracteres) du critere<br />
            <label>Description : <input type="text" name="desc" value="<?= htmlentities($this->current_rule->getDesc()); ?> " /></label>
        </div>
        <h3><a href="#">Bornes de la note</a></h3>
        <div>
            <?php
            $options = $this->current_rule->getData();
            $min = 0;
            $max = 0;

            if (isset($options[0])) {
                $min = intval($options[0]);
            }
            if (isset($options[1])) {
                $max = intval($options[1]);
            }
            ?>
            <div class="field">
                <label>Min : <input type="text" name="choice[]" value="<?= intval($min); ?> " /></label>
            </div>
            <div class="field">
                <label>Max : <input type="text" name="choice[]" value="<?= intval($max); ?> " /></label>
            </div>
        </div>
    </div>
    <br />
    <input type="submit" name="edit" id="addfield" value="Editer le champ" />
</form>
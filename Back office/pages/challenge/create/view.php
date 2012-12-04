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
<form method="post" action="<?= Url::toUserCreate(); ?>">
    <label>Nom d'utilisateur : <input type="text" name="Upseudo" value="" /></label>
    <br /><br/>
    <label>Mot de passe : <input type="password" name="Upasswd" value="" /></label>
    <br /><br/>
    <label>Confirmer le mot de passe : <input type="password" name="Upasswd_confirm" /></label>
    <br /><br />
    <label>Aucun droit <input type="checkbox" name="no_right" checked="checked" value="1" /></label>
    <label>Droit de lecture <input type="checkbox" name="read_right" value="1" /></label>
    <label>Droit d'ecriture <input type="checkbox" name="write_right" value="1" /></label>
    <label>Droit de suppression <input type="checkbox" name="del_right" value="1" /></label>
    <br /><br />
    <input type="submit" name="creer" value="Creer l'utilisateur" />
</form>
<br /><br />
